import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxStoreCouponIssueUser',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/yxStoreCouponIssueUser/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/yxStoreCouponIssueUser',
    method: 'put',
    data
  })
}
