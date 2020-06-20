# game-of-life
Yet another implementation of Conway's Game of Life

If you are using *IntelliJ IDEA*, you can import this repo using **File > New > Project from Version Control... > Git**



## Features

* **Visual themes.** There are three default themes, and you can add more
* **Shortcuts.** There are a number of keyboard shortcuts you can use to control the simulation
* **Patterns.** You can import structures in [RLE](https://www.conwaylife.com/wiki/Run_Length_Encoded) format and then add them in just a few clicks
* **Command line arguments.** You can limit the field, start in windowed mode or specify the initial state
* Unfortunately, there are no ability to save current state/history in a file



## Shortcuts

* `CTRL` - show control panel while pressed
* `ESC` - toggle control panel visibility
* `SPACE` - toggle simulation pause
* `W`/`S` - control speed of simulation
* `A`/`D` - go back / forward in history
* `Q`/`E` - rotate pattern



## Patterns

After first run, the folder `GameOfLife` will be automatically created.
To import game of life structures, create folder `GameOfLife/patterns` and put there a file with any name and your [RLE](https://www.conwaylife.com/wiki/Run_Length_Encoded) as its contents.
It should have only RLE, without additional parameters (such as `x = 3`, `y = 2`, and so on) or comments.
So, finally your folder structure should look like this:
<pre>
GameOfLife/
├───customization
├───themes/
└───patterns/
    └───glider
</pre>
And the `glider` file contents should be somthing like this:
<pre>bo$2bo$3o</pre>



## Command line arguments

* `--size <W> <H>` or `-s <W> <H>` to limit the field with given Width and Height (in cells). This will also start the app in windowed mode
* `--rle` to specify initial state in [RLE](https://www.conwaylife.com/wiki/Run_Length_Encoded) format, for example, `--rle bo$2bo$3o`
