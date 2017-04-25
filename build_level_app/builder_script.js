var CELL_HEIGHT = 32;
var CELL_WIDTH = 32;

var cells_in_row = 32;
var cells_in_col = 24;

var current_x = 1;
var current_y = 0;

var canvas;
var cnv;

var pallet;
var plt;

var objects;

var isOutput = false;

var dictionary = {};
setDictionary();

var game_grid = new Array(cells_in_col);
for(var i=0; i< cells_in_row; i++)
  game_grid[i] = new Array(cells_in_row);


$( document ).ready(function() {
    console.log( "ready!" );  
    objects = new Image();
    objects.src = './resources/object_sprites.png';
    setToggle();
    refreshOutput();
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


  //TODO: Create a 2d array to store the information when adding and removing values.  Then a way to output the string to a file
  if(current_x != 0 || current_y != 0){
    cnv.drawImage(objects, current_x * CELL_WIDTH, current_y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT,i*CELL_WIDTH, j*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT  );
    console.log("game grid add: " + game_grid[i][j]);
    if(game_grid[i][j] != undefined && game_grid[i][j] != ""){
      game_grid[i][j] = game_grid[i][j].concat(":" + dictionary[current_y*cells_in_row + current_x]);
    }else{
      game_grid[i][j] = dictionary[current_y*cells_in_row + current_x];
    }
  }else{
    cnv.clearRect(i*CELL_HEIGHT, j*CELL_WIDTH, CELL_HEIGHT, CELL_WIDTH);
    game_grid[i][j] = "";
  }
  console.log(game_grid[i][j]);
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

//output
function setToggle(){
  $("#output_toggle").click(function() {
    console.log("output: " + isOutput);
    if(isOutput){
      $("#output").css("visibility", "visible");
      $("#output_toggle").text("Hide Output");
      isOutput = false;
    }else{
      $("#output").css("visibility", "hidden");
      $("#output_toggle").text("Show Output");
        isOutput = true;
    }
  });  
}

function refreshOutput(){
  $("#refresh_output").click(function() {
    var output_str = "";
    for(var i = 0; i < cells_in_row; i++) {
      for(var z = 0; z < cells_in_col; z++) {
        if(game_grid[z][i] != undefined){
          output_str = output_str.concat(game_grid[z][i] + ",");
          console.log(game_grid[z][i]);
        }else{
          output_str = output_str.concat(",");
        }
      }
      output_str = output_str.concat("\n");
    }
    console.log("output string: " + output_str);
    $("#output").text(output_str); 
  }); 
}



function setDictionary(){
  //Number on pallet.  Number in Java program
  //Row 1
  dictionary[1] = "b0";
  dictionary[2] = "b1";
  dictionary[3] = "b2";
  dictionary[4] = "b3";
  dictionary[5] = "b4";
  dictionary[6] = "b5";
  dictionary[7] = "b10";
  dictionary[8] = "b11";
  dictionary[9] = "b12";
  dictionary[10] = "b13";
  dictionary[11] = "b14";
  dictionary[12] = "b15";
  dictionary[13] = "b16";
  dictionary[14] = "b17";

  //Row 2
  dictionary[15] = "b20";
  dictionary[16] = "b21";
  dictionary[17] = "b22";
  dictionary[18] = "b23";
  dictionary[19] = "b24";
  dictionary[20] = "b25";
  dictionary[21] = "b26";
  dictionary[22] = "b27";
  dictionary[23] = "b28";
  dictionary[24] = "b29";

  //Row 3
  dictionary[30] = "b30";
  dictionary[31] = "b31";
  dictionary[32] = "b32";
  dictionary[33] = "b33";
  dictionary[34] = "b34";
  dictionary[35] = "b35";
  dictionary[36] = "b36";
  dictionary[37] = "b37";
  dictionary[38] = "b38";

  //Row 4
  dictionary[45] = "b40";
  dictionary[46] = "b41";
  dictionary[47] = "b42";
  dictionary[48] = "b43";
  dictionary[49] = "b44";
  dictionary[50] = "b45";
  dictionary[51] = "b46";
  dictionary[52] = "b47";
  dictionary[53] = "b48";
  dictionary[54] = "b50";
  dictionary[55] = "b51";
  dictionary[56] = "b52";
  dictionary[57] = "b53";
  dictionary[58] = "b54";
  dictionary[59] = "b55";

  //Row 5
  dictionary[60] = "b60";
  dictionary[61] = "b61";
  dictionary[62] = "b62";
  dictionary[63] = "b63";
  dictionary[64] = "b64";
  dictionary[65] = "b65";
  dictionary[66] = "b66";
  dictionary[67] = "b67";
  dictionary[68] = "b68";
  dictionary[69] = "b69";
  dictionary[70] = "b70";
  dictionary[71] = "b71";
  dictionary[72] = "b72";
  dictionary[73] = "b73";
  dictionary[74] = "b74";

  //Row 6
  dictionary[75] = "b80";
  dictionary[76] = "b81";
  dictionary[77] = "b82";
  dictionary[78] = "b83";
  dictionary[79] = "b84";
  dictionary[80] = "b85";
  dictionary[81] = "b86";

  //Row 7
  dictionary[90] = "p";
  dictionary[91] = "e";
  dictionary[92] = "l";
  dictionary[93] = "c1";
  dictionary[94] = "c2";
  dictionary[95] = "d1";
  dictionary[96] = "d2";
  dictionary[97] = "d3";
  dictionary[98] = "d4";
  dictionary[99] = "i0";
  dictionary[100] = "i1";
  dictionary[101] = "i2";
}