[
  '{{repeat(50, 100)}}',
  {
    _id: '{{objectId()}}',
    name: '{{firstName()}} {{surname()}}',
    street: '{{street()}}, #{{integer(1, 200)}}',
    email: '{{email()}}',
    phone: '{{phone()}}'
  }
]
