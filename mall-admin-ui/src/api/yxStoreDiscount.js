import request from '@/utils/request'

export function search(id) {
  return request({
    url: 'api/yxStoreDiscount/search/' + id,
    method: 'get'
  })
}

export function add(data, jsonStr) {
  return request({
    url: 'api/yxStoreDiscount',
    method: 'put',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/yxStoreDiscount/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/yxStoreDiscount',
    method: 'put',
    data
  })
}
