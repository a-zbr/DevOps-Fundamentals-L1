let slideIndex = 1;
    
showSlides(slideIndex);

function plusSlides(n) {
  showSlides(slideIndex += n);
}

function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  // alert(n);
//   console.log('номер слайду', n);
  let i;
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");
  if (n > slides.length) {slideIndex = 1}    
  if (n < 1) {slideIndex = slides.length}
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " active";
}

// // function showName(){
// //     let user = {
// //         name: "Ivan"
// //     }
// //     console.log(user.name);
// // }

// // showName();

// let a = 123;
// console.log("число", a);

// let b = 'Hello!';
// console.log(b);

// let c = true;
// console.log(c);

// let d = null;
// console.log(d);

// let e = undefined;
// console.log(e);

// let user = {
//    name: 'Dima',
//    city: 'Kharkiv',
//    date: 5
// }

// console.log(user);