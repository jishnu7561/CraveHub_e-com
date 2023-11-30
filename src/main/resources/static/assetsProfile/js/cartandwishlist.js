
//==================== add to cart and wishlist ==================

 function addToCart( productId) {
    // event.preventDefault(); // Uncomment this line if you want to prevent default behavior
    console.log("called fn");

    let cartCount = document.getElementById('cartCount');
    // var quantity = 1; // You can allow users to specify the quantity if needed
    console.log(productId);
    $.ajax({
        type: "POST",
        url: "/addToCart",
        data: JSON.stringify({ productId: productId, quantity: 1 }), // You can modify quantity as needed
        contentType: "application/json",
        success: function (response) {
            console.log(response);
            var responseData = JSON.parse(response);

            if (responseData.productExist) {
                // Implement code to update the cart UI as needed
                showAlertBoxAlready();
            } else if (responseData.added == "true") {
                cartCount.innerHTML = Number(responseData.cartCount);
                console.log(responseData.cartCount);
                showAlertBox();
            } else if (responseData.outOfStock) {
                showAlertOutOfStock();
            }
            else{
                window.location.href = '/login';
            }
        },
        error: function () {
            alert("Failed to add the product to the cart.");
        }
    });
}


        function showAlertBox() {
        console.log("alert fun called...");
        $("#cartAlert").fadeIn();

        // Hide the alert box after 5 seconds
        setTimeout(function () {
            $("#cartAlert").fadeOut();
        }, 3000);
    }

    function showAlertBoxAlready() {
        console.log("alert fun called...");
        $("#cartAlertAlready").fadeIn();

        // Hide the alert box after 5 seconds
        setTimeout(function () {
            $("#cartAlertAlready").fadeOut();
        }, 3000);
    }

    function showAlertOutOfStock() {
        console.log("alert fun called...");
        $("#cartAlertOutOfStock").fadeIn();

        // Hide the alert box after 5 seconds
        setTimeout(function () {
            $("#cartAlertOutOfStock").fadeOut();
        }, 3000);
    }


     function addToWishlist( productId) {
    // event.preventDefault(); // Uncomment this line if you want to prevent default behavior
    console.log("called fn wishlist");
    $.ajax({
        type: "POST",
        url: "/addToWishlist",
        data: JSON.stringify({ productId: productId, quantity: 1 }), // You can modify quantity as needed
        contentType: "application/json",
        success: function (response) {
            console.log(response);

            if (response == "exist") {
                showAlertAlready();
            } else if (response == "added") {
                showAlert();
            }
            else{
<!--                window.location.href = '/login';-->
                alert("something wrong")
            }
        },
        error: function () {
            alert("Failed to add the product to the cart.");
        }
    });
}

            function showAlert() {
        console.log("alert fun called...");
        $("#wishlistAlert").fadeIn();

        // Hide the alert box after 5 seconds
        setTimeout(function () {
            $("#wishlistAlert").fadeOut();
        }, 3000);
    }

    function showAlertAlready() {
        console.log("alert fun called...");
        $("#wishlistAlertAlready").fadeIn();

        // Hide the alert box after 5 seconds
        setTimeout(function () {
            $("#wishlistAlertAlready").fadeOut();
        }, 3000);
    }
//==================== /add to cart and wishlist =============================



//================================== single product =================================

 	  function showDiv(){
           let divSH = document.getElementById("divSH").style.display
           let icon = document.getElementById("icon");
           if(divSH=="none"){
               document.getElementById("divSH").style.display="block"
               icon.className = "fa-regular fa-circle-chevron-up"; // Set the class for the displayed icon
 			}
           else{
               document.getElementById("divSH").style.display="none"
               icon.className = "fa-regular fa-circle-chevron-down"; // Set the class for the displayed icon
 		}

       }


           $(document).ready(function() {
               $("#addToCartButton").click(function(event) {
                   event.preventDefault();
 				  let cartCount = document.getElementById('cartCount')
                   var id = $(this).data("id"); // Replace with the actual product ID
                   var quantity = 1; // You can allow users to specify the quantity
                   console.log(id)
                   $.ajax({
                       type: "POST",
                       url: "/addToCart",
                       data: JSON.stringify({ productId: id, quantity: quantity }),

                       contentType: "application/json",
                      success:(response)=>{
                      var responseData = JSON.parse(response);
                      console.log(responseData)
                      if(responseData.productExist){
                           // Implement code to update the cart UI as needed
                           showAlertBoxAlready()
                           }
                      else if(responseData.added == "true") {
                      		cartCount.innerHTML = Number(responseData.cartCount)
                      		console.log(responseData.cartCount)
                       		showAlertBox()
                           }
                      else if(responseData.outOfStock){
                      	showAlertOutOfStock()
                      }
                      else{
                      	alert("please try again.something went wrong !!")
                      }

                       },
                       error: function() {
                           alert("Failed to add the product to the cart.");
                       }
                   });
               });
           });


          function showAlertBox() {
         console.log("alert fun called...");
         $("#cartAlert").fadeIn();

         // Hide the alert box after 5 seconds
         setTimeout(function () {
             $("#cartAlert").fadeOut();
         }, 3000);
     }

     function showAlertBoxAlready() {
         console.log("alert fun called...");
         $("#cartAlertAlready").fadeIn();

         // Hide the alert box after 5 seconds
         setTimeout(function () {
             $("#cartAlertAlready").fadeOut();
         }, 3000);
     }

     function showAlertOutOfStock() {
         console.log("alert fun called...");
         $("#cartAlertOutOfStock").fadeIn();

         // Hide the alert box after 5 seconds
         setTimeout(function () {
             $("#cartAlertOutOfStock").fadeOut();
         }, 3000);
     }


 	  var animation = bodymovin.loadAnimation({
           container: document.getElementById('animContainer'),
           renderer: 'svg',
           loop: true,
           autoplay: true,
           path: 'page404.lottie' // lottie file path
       })
//============================ /single product ===========================

//================================ cart page ================================

 function changeQtyP(productId){
			 let quantityInput = document.getElementById('quantity'+productId)
			 let totalAmount = document.getElementById('totalAmount')
			 let totalSubAmount = document.getElementById('totalSubAmount')

			 let TotalPrice = document.getElementById('Totalprice'+productId)
			 let OgPrice = document.getElementById('price'+productId).innerHTML

				 if (Number(quantityInput.value) < 5) {
 			    $.ajax({
 				      url:'/updateQuantity',
  				     	method:'post',
    				   data:{count:1,productId},
 				      success:(response)=>{
                var responseData = JSON.parse(response);
				if(responseData.success){
                // Update the quantity and total price on the page
                quantityInput.value = responseData.updatedQuantity;
                console.log(responseData.updatedQuantity)
                TotalPrice.innerHTML = '$ ' + (Number(OgPrice) * responseData.updatedQuantity)+'.0';
                 totalAmount.innerHTML = '$' + responseData.totalPrice+'.0';
                  totalSubAmount.innerHTML = '$' + responseData.totalPrice+'.0';
					}
					else{
						showAlertOutOfStock()
					}

 					      }
			     })
 				 }
 				 }

 				    function showAlertOutOfStock() {
					console.log("alert fun called...");
					$("#cartAlertOutOfStock").fadeIn();

					// Hide the alert box after 5 seconds
					setTimeout(function () {
						$("#cartAlertOutOfStock").fadeOut();
					}, 3000);
				}

			function changeQtyM(productId){
			let quantityInput = document.getElementById('quantity'+productId)
			let totalAmount = document.getElementById('totalAmount')
			 let totalSubAmount = document.getElementById('totalSubAmount')
			let TotalPrice = document.getElementById('Totalprice'+productId)
			 let OgPrice = document.getElementById('price'+productId).innerHTML
			 if(Number(quantityInput.value)>1){
 			    $.ajax({
 				      url:'/updateQuantity',
  				     	method:'post',
    				   data:{count:-1,productId},
 				      success:(response)=>{

 				var responseData = JSON.parse(response);

                // Update the quantity and total price on the page
                quantityInput.value = responseData.updatedQuantity;
                console.log(responseData.updatedQuantity)
                TotalPrice.innerHTML = '$ ' + (Number(OgPrice) * responseData.updatedQuantity)+'.0';
                totalAmount.innerHTML = '$' + responseData.totalPrice+'.0';
				 totalSubAmount.innerHTML = '$' + responseData.totalPrice+'.0';
 					      }
			     })
 				 }
			}

//================================ /cart page ================================
