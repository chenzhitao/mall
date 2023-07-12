import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxStoreVisit',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/yxStoreVisit/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/yxStoreVisit',
    method: 'put',
    data
  })
}
