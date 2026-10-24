const allowedDomains = [
    "gmail.com",
    "icloud.com",
    "outlook.com",
    "hotmail.com",
    "live.com"
]

export function emailCheck(email) {
    if (email.includes('@')) {

        let email_pars = email.split('@')

        let before_dog = email_pars[0]
        let after_dog = email_pars[1]

        if (
            before_dog.length <= 4 ||
            before_dog.length >= 30 ||
            !allowedDomains.includes(after_dog)
        ) {
            return false
        }

        return true
    }

    return false
}
