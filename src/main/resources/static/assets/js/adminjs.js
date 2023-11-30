//=====================admin home========================

  //logout
  function logOut() {
      window.location.href = '/logout';
  }

    //sales report in dashboard
      function fetchData(period) {
        let revenue = document.getElementById('revenue')
        let stocks = document.getElementById('stocks')
        let sales = document.getElementById('sales')
          $.ajax({
              method: "POST",
                      url: "/admin/adminHome",
                      data: period,
              success: function(data) {
                      revenue.innerHTML = '₹' + data.revenue;
                      stocks.innerHTML =  data.stocks;
                      sales.innerHTML =  data.sales;
              },
              error: function(error) {
                  console.log(error);
              }
          });
      }

//online and cod graph in dashboard
  function onpageLoad(){
      let codValue = document.getElementById("cod").value
      let onlineValue = document.getElementById("online").value

    var xValues = ["Cash on delivery", "Online Payment"];
    var yValues = [codValue, onlineValue];
    var barColors = [
      "#ffab00",
      "#00d25b"
    ];

    new Chart("TransactionChart", {
      type: "doughnut",
      data: {
        labels: xValues,
        datasets: [{
          backgroundColor: barColors,
          data: yValues
        }]
      },
      options: {
        legend: {
          labels: {
            fontColor: "#fff" // Set label text color to red
          }
        }
      }

    });
  }
  window.onload =onpageLoad()



