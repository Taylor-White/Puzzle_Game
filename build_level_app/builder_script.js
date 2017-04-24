var CELL_HEIGHT = 32;
var CELL_WIDTH = 32;

var current_x = 1;
var current_y = 0;

var canvas;
var cnv;

var pallet;
var plt;

var objects;

$( document ).ready(function() {
    console.log( "ready!" );  
    objects = new Image();
    objects.src = './resources/object_sprites.png';

    setup_pallet_canvas();
    setup_canvas();
    console.log( "Done!" );  
});

//Canvas code
function setup_canvas() {
  canvas = document.getElementById("canvas");
  cnv = canvas.getContext("2d");
  //Eventually get width and height from config file that the main app will use
  var width = CELL_WIDTH*15;
  var height = CELL_HEIGHT*10;
  canvas.width = width;
  canvas.height = height;
  canvas.addEventListener("click", onClickCanvas, false);
  return;
}    

function onClickCanvas(event){
  console.log("onclick canvas");
  updateCanvas(canvas,event);
}
function updateCanvas(canvas, event) {
  console.log("update function");
  var rect = canvas.getBoundingClientRect();
  var x = event.clientX - rect.left;
  var y = event.clientY - rect.top;
  
  var i = Math.floor(x/CELL_WIDTH);
  var j = Math.floor(y/CELL_HEIGHT);

  console.log("x: " + i + " y: " + j);
  console.log(current_x);
  console.log(current_y);

  if(current_x != 0 || current_y != 0){
    cnv.drawImage(objects, current_x * CELL_WIDTH, current_y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT,i*CELL_WIDTH, j*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT  );
  }else{
    cnv.clearRect(i*CELL_HEIGHT, j*CELL_WIDTH, CELL_HEIGHT, CELL_WIDTH);
  }
} 

//Pallet Code
function setup_pallet_canvas() {
  pallet = document.getElementById('tile_canvas'),
  base_image = new Image();
  plt = pallet.getContext('2d');
  base_image.src = './resources/object_sprites.png';
  base_image.onload = function(){
    pallet.width = base_image.width;
    pallet.height = base_image.height;
    plt.drawImage(base_image, 0, 0);
  }  
  pallet.addEventListener("click", onClickPallet, false);

} 
function onClickPallet(event){
  console.log("onclick pallet");
  updateCurrentBrush(event);
}
function updateCurrentBrush(event) {
  console.log("update function");
  var image = document.getElementById('brush');
  var rect = pallet.getBoundingClientRect();
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