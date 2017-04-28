//Pixels in Cell
var CELL_HEIGHT = 32;
var CELL_WIDTH = 32;

//Cells in row/column
var cells_in_row;
var cells_in_col;

var pallet_length = 15;

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

var game_grid;





$( document ).ready(function() {
    console.log( "ready!" );  

    //Initialize Variables
    setCanvasHeight();
    setCanvasLength();
    initializeGrid();
    objects = new Image();
    objects.src = './resources/object_sprites.png';

    //Setup onClicks
    setOutputDisplayToggle();
    refreshOutputButton();
    updateDimensions();
    importClick();

    //Setup Canvas'
    setup_pallet_canvas();
    setup_canvas();
    console.log( "Done!" );  
});

//Canvas code
function setup_canvas() {
  canvas = document.getElementById("canvas");
  cnv = canvas.getContext("2d");
  set_canvas_dim();
  canvas.addEventListener("click", onClickCanvas, false);
  return;
}    
function set_canvas_dim(){
  var width = CELL_WIDTH*cells_in_row;
  var height = CELL_HEIGHT*cells_in_col;
  console.log("WIDTH: " + width);
  canvas.width = width;
  canvas.height = height;
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

  //Place GameObject in cell
    if(current_x != 0 || current_y != 0){
    cnv.drawImage(objects, current_x * CELL_WIDTH, current_y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT,i*CELL_WIDTH, j*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT  );
    if(game_grid[i] == undefined){
      game_grid[i] = new Array();
    }
    if(game_grid[i][j] != undefined && game_grid[i][j] != ""){
      game_grid[i][j] = game_grid[i][j].concat(":" + dictionary[current_y*pallet_length + current_x]);
    }else{
      game_grid[i][j] = dictionary[current_y*pallet_length + current_x];
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

//Output Code
function setOutputDisplayToggle(){
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

function refreshOutputButton(){
  $("#refresh_output").click(refreshOutput); 
}

function refreshOutput(){
    console.log("cells in col: " + cells_in_col);
        console.log("cells in row: " + cells_in_row);
console.log(game_grid);
    var output_str = "";
    //TODO: have a 'mapGrid' function where you pass a function and it applies itself to every element in grid
    for(var i = 0; i < cells_in_col; i++) {
      for(var z = 0; z < cells_in_row; z++) {
        console.log("z: " + z + " i: " + i);
        console.log(game_grid[z][i]);
        if(game_grid[z][i] != undefined){
          output_str = output_str.concat(game_grid[z][i] + ",");
          //console.log(game_grid[z][i]);
        }else{
          output_str = output_str.concat(",");
        }
      }
      if(cells_in_row > 0){
        output_str = output_str.slice(0, -1);
      }
      output_str = output_str.concat("\n");
    }
    console.log("output string: " + output_str);
    $("#output").text(output_str); 
}

function spliceGrid(){
  console.log("cells in col: " + cells_in_col);
  console.log("cells in row: " + cells_in_row);

  console.log("game_grid length: " + game_grid.length);
  console.log("game_grid[0] length: " + game_grid[0].length);

  console.log("BEFORE SPLICE: ");
  console.log(game_grid);
  var game_grid_len = game_grid.length;
  if(cells_in_row < game_grid_len){
    game_grid.splice(cells_in_row,game_grid_len);
  }else if(cells_in_row > game_grid_len){
    var cells_to_add = cells_in_row - game_grid_len;
    for(var i=0; i<cells_to_add; i++){
      game_grid.push(new Array());
    }  
  } 
  game_grid_len = game_grid.length;
  console.log("after first half of splice");
  console.log(game_grid);
  console.log("statistics ");
  console.log("game_grid.length " + game_grid_len);
  console.log("cells_in_col " + cells_in_col);
  for(var i=0; i<game_grid_len; i++){
    if(cells_in_col < game_grid[i].length){
      game_grid[i].splice(cells_in_col,game_grid[i].length);
    }else if(cells_in_col > game_grid[i].length){
      var cells_to_add = cells_in_col - game_grid[i].length;
      console.log("cells to add: " + cells_to_add);
      for(var j=0; j<cells_to_add; j++){
        console.log("push at: " + j);
        game_grid[i].push("");
      }
    }    
  }
  console.log("AFTER SPLICE: ");
  console.log(game_grid);
  
}

function updateDimensions(){
  $("#update_dims").click(function() {
    setCanvasHeight();
    setCanvasLength();
    spliceGrid();
    set_canvas_dim();
    refillCanvasFromGameGrid();
    refreshOutput();
  });  
}  

//Import ~~ Coming soon ~~
function importStringToGrid(str){
  //Get Dimensions
  var rows = str.split(/\r?\n/);
  var len = 0;
  var height = 0;
  for(var i = 0; i < rows.length; i++) {
    var tmp = 0;
    var cells = rows[i].split(/,/);
    for(var z = 0; z < cells.length; z++) {
      tmp = tmp + 1;
    }
    if(tmp > len){
      len = tmp;
    }
    tmp = 0;
    height = height + 1;
  }
  console.log("str len: " + len);
  console.log("str height: " + height);
  
  //Set Canvas Dimensions to match
  
  $("#canvas_height").val(height);
  $("#canvas_length").val(len);
  setCanvasHeight();
  setCanvasLength();

  initializeGrid();
console.log(game_grid);
  //Fill Grid
  for(var i = 0; i < rows.length; i++) {
    var cells = rows[i].split(/,/);
    for(var j = 0; j < cells.length; j++) {
      console.log("cell[" + j + "]: " + cells[j]);
        game_grid[i][j] = cells[j];
    }
  }
  
  //Display Grid
  set_canvas_dim();
  refillCanvasFromGameGrid();
  
  //Refresh output
  refreshOutput();
}

function refillCanvasFromGameGrid(){
  console.log("game_grid: ");
  console.log(game_grid);
  for(var i = 0; i < cells_in_row; i++) {
    var row = game_grid[i];
    for(var j = 0; j < cells_in_col; j++) {
      console.log("row: " + i + " col: " + j);
      if(game_grid[i][j] != undefined && game_grid[i][j] != ""){
        var objects_in_cell = game_grid[i][j].split(":");
        for(var k = 0; k < objects_in_cell.length; k++){
          var draw_location = arraySearch(dictionary,objects_in_cell[k]);
          var pallet_location_x = draw_location % pallet_length;
          var pallet_location_y = Math.floor(draw_location / pallet_length);
          if(!(pallet_location_x == 0 && pallet_location_y == 0)){
            console.log("drawing... object at: " + pallet_location_x + " and " + pallet_location_y);
            cnv.drawImage(objects, pallet_location_x * CELL_WIDTH, pallet_location_y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT,i*CELL_WIDTH, j*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT  );
          } 
        } 
      }
    }
  }
}

//Helper for getting key from value in dictionary
function arraySearch(arr,value) {
    for (var k=0; k<arr.length; k++)
        if (arr[k] === value)                    
            return k;
    return false;
  }

function importClick(){
  $("#import_button").click(function() {
    var str = $("#import_area").val();
    console.log("IMPORT STRING: " + str);
    importStringToGrid(str);
  }); 
}


//Initialize Variables
function setCanvasHeight(){
  cells_in_col = $("#canvas_height").val();
}

function setCanvasLength(){
  cells_in_row = $("#canvas_length").val();
}

function initializeGrid(){
  game_grid = [];
  for(var i=0; i<cells_in_col; i++){
    game_grid[i] = [];
    for(var j=0; j<cells_in_row; j++){
      game_grid[i][j] = "";
    }
  }
  console.log("after grid has been reset: " );
  console.log(game_grid);
      
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
  dictionary[93] = "c0";
  dictionary[94] = "c1";
  dictionary[95] = "d0";
  dictionary[96] = "d1";
  dictionary[97] = "d2";
  dictionary[98] = "d3";
  dictionary[99] = "i0";
  dictionary[100] = "i1";
  dictionary[101] = "i2";

  dictionary.length = 102
}