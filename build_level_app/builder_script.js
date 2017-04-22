
$( document ).ready(function() {
    console.log( "ready!" );  
    setup_pallet_canvas();
    console.log( "Done!" );  
});

function setup_pallet_canvas() {
  var canvas = document.getElementById('tile_canvas'),
  base_image = new Image();
  context = canvas.getContext('2d');
  base_image.src = './resources/object_sprites.png';
  base_image.onload = function(){
    context.width = base_image.width;
    context.height = base_image.height;
    context.drawImage(base_image, 0, 0);
  }  
}	