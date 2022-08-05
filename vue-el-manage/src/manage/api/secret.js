import request from '@MAN/utils/request'

export function getKey() {
  const obj = {
    uri:'auth/login/',
    action: 'getkey'
  }
  return request.json(obj)
}

export function getAESKey(encrypted) {
  const obj = {
    uri:'auth/login/',
    action: 'getaeskey',
    params:{
      aesKey:encrypted
    }
  }
  return request.json(obj)
}
