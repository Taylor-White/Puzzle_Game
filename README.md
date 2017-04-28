# Puzzle_Game

A platformer puzzle game I built to practice programming.

Currently under construction.

Practiced working with object inheritence, GUI, action listeners, sprites, multiple threads, etc.

--------------------------------------------------

## How to play:

Your goal is to collect all the coins.  When you collect all of the coins on a map, the door will open.  Enter the door to reach the next level.

Your character can move left and right and climb up and down ladders.  

Your character can also fire a blaster at the downward diagonal adjacent square to your left or right. If the tile you fire at contains a destructable block, the block will dissapear for a short amount of time, and then reappear several seconds later.  You can fire your blaster while standing or climbing.  Some blocks have reinforced cement, and can not be destroyed.  Be careful when digging.  If a block reappears where you are standing, your player will die and the level will restart.

There are items you can pick up. All items are various types of dynamite.  When you pick one up, it is added to your inventory.  When you drop a dynamite from your inventory, it ignites, and will detonate within a few seconds.  Any blocks caught in the explosion will be temporarily destroyed as if they were hit with your blaster.  There are four types of dynamite, and when they explode the explosion shape is different depending on which type it is.  Be careful.  If your player is caught in the explosion the level will restart.

Use the menu to skip around to various levels.  However, there is currently a bug when skipping levels where the game crashes if too many levels are skipped at once.  

There are only currently six levels, but I will add more over time.  I tried to put them in order of increasing difficulty.  Some of the later levels are quite challenging, but they are all possible.

---------------------------------------------------

## Controls:

* w - move left
* r - move right
* e - climb up
* d - climb down
* s - dig left
* f - dig right
* u - drop first item
* i - drop second item
* o - drop third item
* p - drop fourth item
* x - self destruct, restart the level

--------------------------------------------------

## Screenshots:

![Screenshot_1](/screenshots/game_screenshot_1.png)

![Screenshot_2](/screenshots/game_screenshot_2.png)

![Screenshot_3](/screenshots/game_screenshot_3.png)

## Level Builder App

In addition to the game itself, I created a small Javascript app which can build levels using a GUI.  To build a level, click on an icon in the pallet, and then click where you want it on the main canvas.  Use the eraser to remove blocks from a tile. When you are done, click 'Refresh Output' to display the string which represents the level on the canvas.

Features:

* Displays current brush
* Change dimensions of the level
* Hide/Show output text
* Import levels into the editor

### Screenshots:

![Screenshot_1](/build_level_app/screenshots/level_builder_screenshot_1.png)