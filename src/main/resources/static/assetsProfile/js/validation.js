
//================referral code copy================
    function copyToClipboard() {
        // Get the input element
        var inputElement = document.getElementById('referralCodeInput');

        // Select the text in the input element
        inputElement.select();
        inputElement.setSelectionRange(0, 99999); /* For mobile devices */

        // Copy the selected text to the clipboard
        document.execCommand('copy');

        // Deselect the text (optional)
        inputElement.setSelectionRange(0, 0);

        // Alert the user that the text has been copied (optional)

    }

//============ delete address ===========
    function deleteAddress(addressId) {
        $.ajax({
            url: '/deleteAddress',
            method: 'post',
            data: { addressId },
            success: function (response) {
                showFormDeleteAlert()
            }
        });
    }
          function showFormDeleteAlert(){
                document.getElementById('deleteAlert').style.display = 'block';
                setTimeout(function() {
            document.getElementById('deleteAlert').style.display = 'none';
            }, 5000);
            }

//============ go to addAddress page ==================

        let addrssBtn = document.getElementById("addressBtn");
        let addreslinkBtn = document.getElementById("tab-addAddress-link");
        addrssBtn.addEventListener('click',()=>{
        addreslinkBtn.click()
        })


//================= address validation ==================
    function validateUserAddressForm() {
    var country = document.getElementById("Countryadd").value;
    var fullName = document.getElementById("fullNameadd").value;
    var mobileNumber = document.getElementById("mobileNumberadd").value;
    var city = document.getElementById("cityadd").value;
    var state = document.getElementById("stateadd").value;
    var pincode = document.getElementById("pincodeadd").value;
    var ValidErrMess = document.getElementById("ValidErrMess");


    if (!country.trim() || !fullName.trim() || !mobileNumber.trim() || !city.trim() || !state.trim() || !pincode.trim()) {
        // alert("All fields marked as required must be filled out.");
        ValidErrMess.innerHTML = "All fields are Required must be filled out"
        showFormValidAlert()
        return false;
    }

    if (!(mobivalidateMobileNumberleNumber)) {
        // alert("Invalid mobile number. It should be a valid number between 10 and 12 digits.");
        ValidErrMess.innerHTML = "Invalid mobile number. It should be a valid number between 10 and 12 digits."
        showFormValidAlert()
        return false;
    }

    if (!validatePincode(pincode)) {
        // alert("Invalid pin code. It should be a valid 6-digit number.");
        ValidErrMess.innerHTML = "Invalid pin code. It should be a valid 6-digit number."
        showFormValidAlert()
        return false;
    }

    return true;
}

function validateMobileNumber(mobileNumber) {
    mobileNumber = mobileNumber.replace(/\D/g, "");
    return mobileNumber.length == 10 ;
}

function validatePincode(pincode) {
    return !isNaN(pincode);
}


    setTimeout(function() {
    document.getElementById('myAlert').style.display = 'none';
}, 5000);


    function showFormValidAlert(){
        document.getElementById('validateAlert').style.display = 'block';
        setTimeout(function() {
    document.getElementById('validateAlert').style.display = 'none';
    }, 5000);
    }



//=================== user-details edit validation ===================
    function validateUserDetailsForm() {
       var firstName= document.getElementById("firstNameEdit").value;
       var lastName= document.getElementById("lastNameEdit").value;
       var userName= document.getElementById("userNameEdit").value;
       var email= document.getElementById("emailIdEdit").value;
       var ValidErrMess = document.getElementById("ValidErrMess");

       var regexFirstName = /^[A-Za-z]{4,15}$/; // First name should be between 4 and 15 characters
       var regexLastName = /^[A-Za-z]{1,15}$/;
       var regexEmail = /^[a-z-0-9._-]+@[a-z-0-9.-]+\.[a-z]{2,4}$/;
       var regexUsername = /^[A-Za-z0-9]{4,10}$/;

       if (!firstName.trim() || !lastName.trim() || !userName.trim() || !email.trim()) {
           // alert("All fields marked as required must be filled out.");
           ValidErrMess.innerHTML = "All fields are Required must be filled out"
           showFormValidAlert()
           return false;
       }

       if (!regexFirstName.test(firstName)) {
   	       ValidErrMess.innerHTML = "First name must contain only alphabets and be between 5-15 characters."
           showFormValidAlert()
           return false;
       }

      if (!regexLastName.test(lastName)) {
           ValidErrMess.innerHTML = "Last name must contain only alphabets and be up to 15 characters."
           showFormValidAlert()
           return false;
       }

       if (! regexUsername.test(userName)) {
           ValidErrMess.innerHTML = "Username must contain only alphabets and numbers and be between 4-10 characters."
           showFormValidAlert()
           return false;
       }

       if (!regexEmail.test(email)) {
           ValidErrMess.innerHTML = "Email is not valid."
           showFormValidAlert()
           return false;
       }

       return true;
   }

//  ===================== change password validation ==================
 function validateChangePasswordForm() {
    var newPassword= document.getElementById("newPassword").value;
    var confirmPassword= document.getElementById("confirmPassword").value;
    var ValidErrMess = document.getElementById("ValidErrMess");

    var regexPassword = /^[A-Za-z0-9]{4,8}$/;

    if (!newPassword.trim() || !confirmPassword.trim()) {
        // alert("All fields marked as required must be filled out.");
        ValidErrMess.innerHTML = "All fields are Required must be filled out"
        showFormValidAlert()
        return false;
    }

    if (!regexPassword.test(newPassword)) {
	ValidErrMess.innerHTML = "New Password must contain min 4 and max 8 alphabets or numbers."
        showFormValidAlert()
        return false;
    }

   if (!regexPassword.test(confirmPassword)) {
        ValidErrMess.innerHTML = "Confirm Password must contain min 4 and max 8 alphabets or numbers."
        showFormValidAlert()
        return false;
    }

    return true;
}

// ================== referral code ===============

function validateReferralCodeForm() {
    var referralCode= document.getElementById("referralCodeInput1").value;

    if (!referralCode.trim()) {
        // alert("All fields marked as required must be filled out.");
        ValidErrMess.innerHTML = "Fields are Required must be filled out"
        showFormValidAlert()
        return false;
    }
    return true;
}

//=========================== category =====================

function validateCatAndSubCatForm() {
    var InputTextName= document.getElementById("InputTextName").value;

    var regexName = /^[a-zA-Z]+$/;

    if (!InputTextName.trim()) {
        // alert("All fields marked as required must be filled out.");
        ValidErrMess.innerHTML = "All fields are Required must be filled out"
        showFormValidAlert()
        return false;
    }

    if (!regexName.test(InputTextName)) {
	ValidErrMess.innerHTML = "Field should contain only alphabets."
        showFormValidAlert()
        return false;
    }

    return true;
}

//============================ add coupon =================================

function validateAddCouponForm(){
           var couponCode= document.getElementById("couponCode").value;
           var expiryDate= document.getElementById("datepicker").value;
           var amount= document.getElementById("amount").value;
           var minAmount= document.getElementById("minAmount").value;
           var description= document.getElementById("description").value;
           var maxUsers = document.getElementById("maxUsers").value;
           var ValidErrMess = document.getElementById("ValidErrMess");

          var regexCouponCode = /^[A-Za-z0-9]{4,10}$/;

            var regexDescription = /^(?:[a-zA-Z0-9_\-,.]\s*){10,}$/;



           if (!couponCode.trim() || !expiryDate.trim() || !amount.trim() || !minAmount.trim() || !description.trim() || !maxUsers.trim()) {
               // alert("All fields marked as required must be filled out.");
               ValidErrMess.innerHTML = "All fields are Required must be filled out"
               showFormValidAlert()
               return false;
           }

           if (!regexCouponCode.test(couponCode)) {
       	       ValidErrMess.innerHTML = "Code should contain only alphabets and numbers of max 10 characters."
               showFormValidAlert()
               return false;
           }

          if (!regexDescription.test(description)) {
               ValidErrMess.innerHTML = "Description should contain min 10 characters."
               showFormValidAlert()
               return false;
           }

           if (amount < 1) {
               ValidErrMess.innerHTML = "Offer amount should be greater than one."
               showFormValidAlert()
               return false;
           }

           if (minAmount < 50) {
               ValidErrMess.innerHTML = "Minimum purchase amount should be greater than 50."
               showFormValidAlert()
               return false;
           }

           if (maxUsers < 1) {
                          ValidErrMess.innerHTML = "Maximum users usage count should be greater 0."
                          showFormValidAlert()
                          return false;
                      }

           return true;

}

//============================ offer ==============================

function validateOfferForm(){
           var offerName= document.getElementById("offerName").value;
           var percentage= document.getElementById("percentage").value;
           var startDate= document.getElementById("startDate").value;
           var endDate= document.getElementById("endDate").value;
           var ValidErrMess = document.getElementById("ValidErrMess");
//            alert(offerName)
//            alert(percentage)
//            alert(startDate)
//            alert(endDate)

            var offerNameReg = /^[A-Za-z\s]{1,20}$/;

           if (!offerName.trim() || !percentage.trim() || !startDate.trim() || !endDate.trim()) {
               // alert("All fields marked as required must be filled out.");
               ValidErrMess.innerHTML = "All fields are Required must be filled out"
               showFormValidAlert()
               return false;
           }

           if (!offerNameReg.test(offerName)) {
       	       ValidErrMess.innerHTML = "offerName should contain only alphabets of max 15 characters."
               showFormValidAlert()
               return false;
           }

           if (percentage < 1) {
               ValidErrMess.innerHTML = "Offer percentage should be greater than one."
               showFormValidAlert()
               return false;
           }

           return true;

}

//==================== referral link ==============
function validateReferralLinkForm() {
       var email= document.getElementById("emailId").value;
       var ValidErrMess = document.getElementById("ValidErrMess");


       var regexEmail = /^[a-z-0-9._-]+@[a-z-0-9.-]+\.[a-z]{2,4}$/;

       if (!email.trim()) {
           // alert("All fields marked as required must be filled out.");
           ValidErrMess.innerHTML = " field must be filled out"
           showFormValidAlert()
           return false;
       }

       if (!regexEmail.test(email)) {
           ValidErrMess.innerHTML = "Email is not valid."
           showFormValidAlert()
           return false;
       }

       return true;
   }

