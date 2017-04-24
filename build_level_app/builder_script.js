var CELL_HEIGHT = 32;
var CELL_WIDTH = 32;

var current_x = 1;
var current_y = 0;

$( document ).ready(function() {
    console.log( "ready!" );  
    setup_pallet_canvas();
    console.log( "Done!" );  
});

function setup_pallet_canvas() {
  var canvas = document.getElementById('tile_canvas'),
  base_image = new Image();
    pallet = document.getElementById("tile_canvas"),

  context = canvas.getContext('2d');
  base_image.src = './resources/object_sprites.png';
  base_image.onload = function(){
    canvas.width = base_image.width;
    canvas.height = base_image.height;
    context.drawImage(base_image, 0, 0);
  }  
  pallet.addEventListener("click", onClickPallet, false);

}	

function onClickPallet(event){
  console.log("onclick pallet");
  updateCurrentBrush(pallet,event);
}
function updateCurrentBrush(canvas, event) {
  console.log("update function");
  var image = document.getElementById('brush');
  var rect = canvas.getBoundingClientRect();
  var x = event.clientX - rect.left;
  var y = event.clientY - rect.top;
  
  var i = Math.floor(x/CELL_HEIGHT);
  var j = Math.floor(y/CELL_WIDTH);

  console.log("x: " + i + " y: " + j);
  current_x = i;
  current_y = j;

  $("#brush_spot").css("background-position", "-" + current_x * CELL_WIDTH + "px " + "-" + current_y * CELL_HEIGHT + "px");
  console.log("finished");
} 