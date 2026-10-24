export function getPasswordScore(password){
    let score = 0

    if (password.length >= 8) score++
    if (/[A-Z]/.test(password)) score++
    if (/\d/.test(password)) score++
    if (/[^\w\s]/.test(password)) score++
    return score
}