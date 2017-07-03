[
  '{{repeat(3)}}',
  {
    _id: '{{objectId()}}',
    mainTitle: function () {
      var titles = ['Today', 'This week', 'Upcoming'];
      return titles[Math.floor(Math.random()*titles.length)];
    },
    secondaryTitle: function () {
      var titles = ['Tue, Jun 6', 'Wed, Jun 7 - Sun, Jun 11', 'After this week'];
      return titles[Math.floor(Math.random()*titles.length)];
    },
    orders: [
      '{{repeat(2, 4)}}',
      {
        _id: '{{objectId()}}',
        customer: {
          name: '{{firstName()}} {{surname()}}',
          number: '{{phone()}}'
        },
        status: function () {
          var statuses = ['ready', 'delivered', 'problem', 'new'];
          return statuses[Math.floor(Math.random()*statuses.length)];
        },
        place: function () {
          var places = ['Store', 'Bakery'];
          return places[Math.floor(Math.random()*places.length)];
        },
        date: '{{date(new Date(2014, 0, 1), new Date())}}',
        goods: [
          '{{repeat(2, 4)}}',
          {
            count: '{{integer(1, 20)}}',
            name: '{{lorem(2, "words")}}',
            description: '{{lorem(5, "words")}}',
            unitPrice: '{{integer(1, 100, )}}'
          }
        ],
        details: '{{lorem(5, "words")}}',
        comments: [
          '{{repeat(2, 4)}}',
          {
            message: '{{lorem(3, "words")}}'
          }
        ],
        totalPrice: '{{integer(10, 500, )}}',
        orderNumber: '{{integer(10, 1000, )}}',
        history: [
          '{{repeat(2, 4)}}',
          {
            name: '{{firstName()}}',
            date: '{{date(new Date(2014, 0, 1), new Date())}}',
            status: function () {
              var statuses = ['ready', 'delivered', 'problem', 'new'];
              return statuses[Math.floor(Math.random()*statuses.length)];
            }
          }
        ]
      }
    ]
  }
]
