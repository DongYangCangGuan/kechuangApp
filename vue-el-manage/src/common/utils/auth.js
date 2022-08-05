import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
    return Cookies.get(TokenKey)
}

export function setToken(token) {
    return Cookies.set(TokenKey, token)
}

export function removeToken() {
    return Cookies.remove(TokenKey)
}

const WebSecretKey = 'Web-Secret-Key'

export function getSecret() {
    return Cookies.get(WebSecretKey)
}

export function setSecret(secretKey) {
    return Cookies.set(WebSecretKey, secretKey)
}

export function removeSecret() {
    return Cookies.remove(WebSecretKey)
}
