[
  '{{repeat(100)}}',
  {
    _id: '{{objectId()}}',
    productName: function () {
      var cache = window._cache = window._cache || [];
      
      var ingr = ['Blueberry', 'Raspberry', 'Strawberry', 'Vanilla', 'Cheese', 'Cream', 'Chocolate'];
      var prod = ['Cake', 'Cookie', 'Biscuit', 'Muffin', 'Tart', 'Pastry', 'Cracker'];

      var first = Math.floor(Math.random()*ingr.length);
      var second = Math.floor(Math.random()*ingr.length);
      var third = Math.floor(Math.random()*prod.length)
      
      var name;
      while (!name || cache.indexOf(name) >=0) {
        name = ingr[first] + ((first != second) ? ' ' + ingr[second] : '') + ' '  + prod[third];
      }
      
      return name;
      
    },
    unitPrice: '{{integer(1000, 3000, )}}'
  }
]