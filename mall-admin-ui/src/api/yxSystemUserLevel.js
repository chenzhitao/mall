import request from '@/utils/request'

export function getLevels(params) {
  return request({
    url: 'api/yxSystemUserLevel',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'api/yxSystemUserLevel',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/yxSystemUserLevel/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/yxSystemUserLevel',
    method: 'put',
    data
  })
}

export default { add, edit, del, getLevels }
