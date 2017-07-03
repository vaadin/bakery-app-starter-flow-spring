[
  '{{repeat(50, 100)}}',
  {
    _id: '{{objectId()}}',
    name: '{{firstName()}}',
    last: '{{surname()}}',
    picture: function() {
      return 'https://randomuser.me/api/portraits/thumb/women/' + (Math.floor(Math.random() * 99) + 1)  + '.jpg';
    },
    email: '{{email()}}',
    password: 'abc' + '{{integer(1000,9999)}}',
    role: function () {
      var roles = ['Barista', 'Admin', 'Baker'];
      return roles[Math.floor(Math.random()*roles.length)];
    }
  }
]