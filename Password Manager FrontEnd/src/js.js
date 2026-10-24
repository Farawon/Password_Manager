import { apiFetch } from "./api.js";

const stripe = Stripe("pk_test_51UCP92K2Q7o6r3hoRnh5QNuvRMiEbBkQezMu9dvWvwlLcXOTh9XRhaEnIXncNsaK3XgT1Sdk9s8Gyxa0YHYJ2BjL00vrRIGOun");

const token = localStorage.getItem('token');

const cardsContainer = document.getElementById("cards");
const addCardButton = document.getElementById("add-card-button");
const newCardContainer = document.getElementById("new-card");
const payButton = document.getElementById("pay-button");

const errorElement = document.getElementById("error");
const successElement = document.getElementById("success");

const elements = stripe.elements();

const cardElement = elements.create("card");
cardElement.mount("#card-element");

let selectedPaymentMethodId = null;
let cards = [];
async function loadCards() {

    errorElement.textContent = "";

    try {

        const response = await apiFetch(
            "/card",
            {
                method: "GET",
            }
        );

        if (!response.ok) {
            throw new Error(
                `Failed to load cards: ${response.status}`
            );
        }

        cards = await response.json();

        renderCards();

    } catch (error) {

        console.error(error);

        errorElement.textContent =
            error.message;
    }
}


function renderCards() {

    cardsContainer.innerHTML = "";

    if (cards.length === 0) {

        cardsContainer.innerHTML =
            "<p>You don't have any saved cards.</p>";

        newCardContainer.style.display = "block";
        addCardButton.style.display = "none";

        return;
    }

    cards.forEach((card, index) => {

        const div = document.createElement("div");

        div.classList.add("card");

        div.innerHTML = `
            <div class = 'cardCommonInfo'>
                <i class="fa-solid fa-credit-card cardModal"></i>
                <div class = 'cardCommon'>
                    <strong class = 'cardStrong'>Card ${index + 1}</strong>
                    ${'Visa ending in ' + card.maskedCard}
                    <div class = 'expiresDate'>Expires: ${card.expDate}</div>
                </div>
            </div>
        `;

        div.addEventListener("click", () => {

            document
                .querySelectorAll(".card")
                .forEach(element => {
                    element.classList.remove("selected");
                });

            div.classList.add("selected");

            selectedPaymentMethodId = card.pmId;

            console.log(
                "Selected payment method:",
                selectedPaymentMethodId
            );
        });

        cardsContainer.appendChild(div);
    });

    const firstCard = cardsContainer.querySelector(".card");

    if (firstCard) {

        firstCard.classList.add("selected");

        selectedPaymentMethodId = cards[0].pmId;
    }
}

addCardButton.addEventListener("click", () => {

    newCardContainer.style.display = "block";

    addCardButton.style.display = "none";

    selectedPaymentMethodId = null;
});


async function createNewCard() {

    const {
        paymentMethod,
        error
    } = await stripe.createPaymentMethod({
        type: "card",
        card: cardElement
    });

    if (error) {
        throw new Error(error.message);
    }

    console.log(
        "Created PaymentMethod:",
        paymentMethod.id
    );

    const response = await apiFetch(
        "/card",
        {
            method: "POST",
            body: JSON.stringify({
                paymentMethodId: paymentMethod.id
            })
        }
    );

    if (!response.ok) {

        const text = await response.text();

        throw new Error(
            `Add card failed: ${response.status} ${text}`
        );
    }

    const savedCard = await response.json();

    console.log("Saved card:", savedCard);

    return savedCard.pmId;
}


async function createPayment(paymentMethodId, plan) {

    const response = await apiFetch(
        "/payment",
        {
            method: "POST",

            body: JSON.stringify({
                paymentMethodId: paymentMethodId,
                plan: plan
            })
        }
    );

    if (!response.ok) {

        const text = await response.text();

        throw new Error(
            `Payment failed: ${response.status} ${text}`
        );
    }

    return await response.json();
}


payButton.addEventListener("click", async () => {

    errorElement.textContent = "";
    successElement.textContent = "";

    payButton.disabled = true;
    payButton.textContent = "Processing...";

    try {

        const selectedPlan =
            document.querySelector(
                'input[name="plan"]:checked'
            ).value;

        if (!selectedPaymentMethodId) {

            if (cards.length === 0 ||
                newCardContainer.style.display !== "none") {

                selectedPaymentMethodId =
                    await createNewCard();
            }
        }


        const paymentResult =
            await createPayment(
                selectedPaymentMethodId,
                selectedPlan
            );

        console.log(
            "Payment result:",
            paymentResult
        );

        if (paymentResult.paymentStatus === "PENDING") {

            if (!paymentResult.clientSecret) {

                throw new Error(
                    "Payment is pending, but clientSecret is missing"
                );
            }

            const {
                paymentIntent,
                error
            } = await stripe.confirmCardPayment(
                paymentResult.clientSecret
            );

            if (error) {
                throw new Error(error.message);
            }

            console.log(
                "PaymentIntent:",
                paymentIntent
            );

            if (
                paymentIntent.status === "succeeded"
            ) {

                successElement.textContent =
                    "Payment successful!";

            } else {

                successElement.textContent =
                    `Payment status: ${paymentIntent.status}`;
            }

        } else {

            successElement.textContent =
                "Subscription created successfully.";
        }

    } catch (error) {

        console.error(error);

        errorElement.textContent =
            error.message;

    } finally {

        payButton.disabled = false;
        payButton.textContent = "Subscribe";
    }
});


loadCards();