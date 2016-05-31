# ImageBook
A book add-on for Minecraft.


## Overview
imageBook is an add-on for Minecraft that allows you to add a book to the game.  The pages of the book are made up
of individual images rather than text.  This means pages can be created in an external image editor with no restriction
on fonts and layout.

ImageBook is aimed at mod pack developers who wish to add a book to the game for things like tutorials and custom
recipe descriptions.

This software is free to use and may be added to any mod pack.  No permission is necessary.  The source code may also
be freely used and copied for other mods.

This mod is for Minecraft version 1.7.10 only.


## Download

The mod is located at https://github.com/MattJohns/ImageBook/blob/master/imagebook-0.1.jar .  This should go in the mods folder
of your minecraft directory, alongside the other mods for your pack.


## Usage
This mod consists of a 3 parts: The mod itself, the configuration file, and the images for each page of the book.

The configuration file 'imagebook.json' should go in the config folder of your minecraft directory.  Please see the configuration section
below for the format of the file.

Images for the pages of the book should go in a sub folder within your config folder.  They can be placed elsewhere but this
is the recommended location.  The sub folder should be named after the content of the book, for example 'MagicRecipies3' .

Once those are in place the mod should automatically load when you run the game.


## Error Log
If the book is not loading, please check the error log for any messages.  If there is an image missing the book will not load and will
not display an error to the user in-game.  It will only write error messages to the log file which can be found in your logs folder in the minecraft
directory, under the name `fml-client-latest.log` .

Search for any messages from 'ImageBook' within that file to find where the error is occuring.  Please contact me if you are not able to
understand the error message.


## Configuration
The configuration file is more complicated than most other mods as it needs to hold multiple books with multiple pages in each book, as
well as information about margins and the book icon itself.  The format is also different than other mods (.json) because the standard
forge configuration files are not able to contain nested arrays.

Each field of the configuration file is described below.  As I understand the format is complicated I'll do my best to expand on these sections
until they are as clear as possible.  If there is any misunderstanding please contact me and I'll update the appropriate section with
more information.


### General Structure
This mod may contain multiple books.  The root element "bookList" contains all the information about each book including the pages
for the books.  Because "bookList" is an array it must have all of the books within it contained within square brackets ('[' and ']').

Each book is wrapped by curly braces ('{' and '}') and terminated with a comma.  The format is:

```
"bookList" : [
	{book1},
	{book2},
	{book3}
]
```

Note the final book should not be terminated by a comma (as per JSON formatting specifications).

Each book also contains fields that describe the book, such as name, icon, and an array of page images.  These fields are described
below.


### Layout
Books and their pages are laid out using sizes that are relative to their container.  This means pages are relative to the book and the book
is relative to the screen.

Sizes and positions are specified as a percentage, where 0.0 is 0 percent and 1.0 is 100 percent.  So if a book has a width of 0.25
it will take up a quarter of the screen.

The reason proportional sizes are used by this mod is because Minecraft uses a fairly complicated system of sizing images that
are displayed on the screen.  An image that is 100x100 pixels will not be displayed in that size because it is affected by the GUI Scale
option in the game's video settings screen.  The end result is that if a book was made using absolute pixel sizes it would sometimes
take up the whole screen and other times take up a tiny fraction of the screen depending on that setting.

And so by using relative, proportional sizes and positions it is possible to have a consistent size of the book regardless of screen resolution,
Minecraft GUI scale and screen aspect ratio.

This may cause issues where text is not rendered cleanly due to this automatic scaling of the images that hold the page text.  In the future
there will be an option to ignore automatic scaling and render the image in exact pixel dimensions instead.  Currently there is the option
`isPageKeepAspectRatio` which goes some way to counter the problem of stretching.


### Description
The `description` field simply describes the configuration file.  This field does not need to be changed and can be ignored.

Example:

```
{
	"description" : "Configuration file for imagebook mod."
}
```


### Help URL
The `helpUrl` field is a link to a page that describes the format of the configuration file.  This field does not need to be changed and
can be ignored.

Example:

```
{
	"helpUrl" : "https://github.com/MattJohns/ImageBook"
}
```


### Configuration Version
The `configurationVersion` field sets the version of the configuration file.  This allows the mod to load older configurations correctly.

Example:

```
{
	"configurationVersion" : "0.1"
}
```


### Resource Packs
The `resourcePackList` field is an array which holds a list of resource packs that contain images for the books.  This field is currently not used
but will be implemented in future versions.


### Book List
The `bookList` field is an array which holds all of the books and their information such as page images, margin sizes and the book icon to display
for the user.

Example:

```
{
	"bookList" : [
	]
}
```


### Internal Name
The `nameInternal` field contains the name for the book used internally by the mod.  This should simply be set to the same as the displayName
field.  The reason this field exists is to allow for multiple books that have the same name.  In that situation the internal name should be set to
something unique so the mod can tell the difference between the books with the same display name.

Example:

```
{
	"bookList" : [
		"nameInternal" : "Example Book"
	]
}
```


### Display Name
The `nameDisplay` field contains the name for the book that will be displayed to the user (for example when they hover the mouse over the
book in their inventory).  It may be the same as other books in the mod.

Example:

```
{
	"bookList" : [
		"nameDisplay" : "Example Book"
	]
}
```


### Book Left Margin
The `bookContainerMarginLeft` field specifies the amount of space between the left side of the book and the left side of the screen.

Note this field uses proportional units, so 0.25 means 25% of the screen.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"bookContainerMarginLeft" : 0.1
	]
}
```


### Book Right Margin
The `bookContainerMarginRight` field specifies the amount of space between the right side of the book and the right side of the screen.

Note this field uses proportional units, so 0.25 means 25% of the screen.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"bookContainerMarginRight" : 0.1
	]
}
```


### Book Top Margin
The `bookContainerMarginTop` field specifies the amount of space between the top of the book and the top of the screen.

Note this field uses proportional units, so 0.25 means 25% of the screen.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"bookContainerMarginTop" : 0.1
	]
}
```


### Book Bottom Margin
The `bookContainerMarginBottom` field specifies the amount of space between the bottom of the book and the bottom of the screen.

Note this field uses proportional units, so 0.25 means 25% of the screen.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"bookContainerMarginBottom" : 0.1
	]
}
```


### Page Outside Margin
The `pageMarginOutside` field specifies the amount of space between the left side of pages and the left side their book (for pages on the left
hand side), or the right side of pages and the right side of the book (for pages on the right hand side).

In other words it specifies the distance between the edge of the book and the edge of the page.

Note this field uses proportional units, so 0.25 means 25% of the book.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"pageMarginOutside" : 0.1
	]
}
```
 

### Page Inside Margin
The `pageMarginInside` field specifies the amount of space between the right side of pages and the center their book (for pages on the left
hand side), or the left side of pages and the center of the book (for pages on the right hand side).

In other words it specifies the distance between the center of the book and the edge of the page.

Note this field uses proportional units, so 0.25 means 25% of the book.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"pageMarginInside" : 0.1
	]
}
```


### Page Top Margin
The `pageMarginTop` field specifies the amount of space between the top of pages and the top of their book.

Note this field uses proportional units, so 0.25 means 25% of the book.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"pageMarginTop" : 0.1
	]
}
```


### Page Bottom Margin
The `pageMarginBottom` field specifies the amount of space between the bottom of pages and the bottom of their book.

Note this field uses proportional units, so 0.25 means 25% of the book.

As this field is numeric it does not require quotes around the value.

Example:

```
{
	"bookList" : [
		"pageMarginBottom" : 0.1
	]
}
```


### Keep Book Aspect Ratio
The `isBookContainerKeepAspectRatio` field is not currently used and should always be set to `true`.


### Keep PageAspect Ratio
The `isPageKeepAspectRatio` field is not currently used and should always be set to `true`.


### Book Icon
The 'icon' field contains an icon for the book.  This will be displayed in the player's inventory when they hold
the book.

Example:

```
{
	"bookList": [
		"icon": {
		}
	]
}
```


### Book Image
The `bookContainerImage` field contains an image for the background of the book.  This should be an image of
paper or something similar.  It contains multiple fields that are listed in the `Image` section below.

Example:

```
{
	"bookList": [
		"bookContainerImage": {
		}
	]
}
```


### Page Image List
The 'pageImageList' field is an array that contains the images to use for each page.  The order pages are listed in
the file is the order that will be used to display them in the book.

See the `Image` section below for details on the field format of images.

Example:

```
{
	"bookList": [
		"pageImageList": [
		]
	]
}
```


### Image
Images are specified by multiple fields that describe the image file location, image type and other information.  A description of
each field is below.

Currently only PNG image formats are supported and transparency is not supported.  That means care must be taken to
line up the background image for a book with its pages.  For example if the background is paper with lines ruled across it
then the individual page images should also have that same background, with spacing that takes the margins into account.


### Image - Type
The `imageType' field describes the location and format of the image.  There are 5 different types:

`EXTERNAL_FILE` - An image file that exists within the minecraft folder.  This path should be specified in the `externalPathRelative`
field.

`IMAGEBOOK_RESOURCE` - An image file that is embedded within this mod.  Currently there are a couple of test images that can
be used such as `test512x512.png` .

`INTERNAL` - An image that is part of the vanilla Minecraft, or part of a mod that is loaded.  The full name of the image should be used
such as `minecraft:chest` .

`RESOURCE_PACK` - An image that is part of a resouce pack.  This field is currently not used.

`SOLID_COLOR` - A simple colored image generated by the mod.  This field is not currently used.


### Image - External Path
The `externalPathRelative` field specfies the path to an image file that exists on the user's hard drive.  The path should be relative
to the minecraft folder and use double backslashes between folders.

This field is only used when accompanied by an image type of `EXTERNAL_FILE` .

Example:

```
{
	"bookList": [
		"pageImageList": [
			{
				"imageType": "EXTERNAL_FILE",
				"externalPathRelative": "config\\ExampleBook\\Page2.png"
			}
		]
	]
}
```


### Image - Internal Filename
The `internalFilename` field speicfies the filename of an image that exists as a java resource.

This field should only be used when accompanied by an image type of `IMAGEBOOK_RESOURCE`, `INTERNAL` or `RESOURCE_PACK`.

```
{
	"bookList": [
		"pageImageList": [
			{
				"imageType": "IMAGEBOOK_RESOURCE",
				"internalFilename": "test512x512.png"
			}
		]
	]
}
```


### Image - Pack ID
The `packId` field is used for images that are contained within resource packs.  It is currently not used.


### Image - Solid Color
The `solidColor` field specifies a color to use instead of an image.  It is currently not used.


### Image - Repeat
The `isRepeat` field specifies whether the image should be repeated in any empty space within the image's area.


### Image - Repeat Ratio X
The `repeatRatioX` field specifies how often to repeat the image within the image's layout area, in the horizontal direction.

Note this field is proportional so a value of 3 will repeat the image 3 times.  A value of 0.5 will only draw half of the image.


### Image - Repeat Ratio Y
The `repeatRatioY` field specifies how often to repeat the image within the image's layout area, in the vertical direction.

Note this field is proportional so a value of 3 will repeat the image 3 times.  A value of 0.5 will only draw half of the image.

## Example Configuration File
This file shows an example configuration.  Note it requires a subfolder in your minecraft config folder called `ExampleBook`
which contains 3 images called `Background.png`, `Page1.png` and `Page2.png`.  Any images will do for testing.

The configuration file itself should be named `imagebook.json` and exist in your minecraft config folder.

Please take careful note of the commas and brackets which need to be present otherwise an error will occur.  Refer to the JSON
specfication for more details or feel free to email me.

Contents of example imagebook.json:

```
{
  "description": "Configuration file for ImageBook mod.",
  "helpUrl": "https://github.com/MattJohns/ImageBook",
  "configurationVersion": "0.1",
  "resourcePackList": [],
  "bookList": [
    {
      "nameInternal": "ExampleBook",
      "nameDisplay": "Example Book",
      "bookContainerMarginLeft": 0.1,
      "bookContainerMarginRight": 0.1,
      "bookContainerMarginTop": 0.1,
      "bookContainerMarginBottom": 0.1,
      "pageMarginOutside": 0.1,
      "pageMarginInside": 0.02,
      "pageMarginTop": 0.1,
      "pageMarginBottom": 0.1,
      "isBookContainerKeepAspectRatio": true,
      "isPageKeepAspectRatio": true,
      "bookContainerImage": {
        "imageType": "EXTERNAL_FILE",
        "externalPathRelative": "config\\ExampleBook\\Background.png",
        "internalFilename": "",
        "packId": "",
        "solidColor": -1,
        "isRepeat": true,
        "repeatRatioX": 1.0,
        "repeatRatioY": 1.0
      },
      "pageImageList": [
        {
          "imageType": "EXTERNAL_FILE",
          "externalPathRelative": "config\\ExampleBook\\Page2.png",
          "internalFilename": "Pa.png",
          "packId": "imagebook2",
          "solidColor": -1,
          "isRepeat": true,
          "repeatRatioX": 1.0,
          "repeatRatioY": 1.0
        },
        {
          "imageType": "EXTERNAL_FILE",
          "externalPathRelative": "config\\ExampleBook\\Page2.png",
          "internalFilename": "",
          "packId": "",
          "solidColor": -1,
          "isRepeat": true,
          "repeatRatioX": 1.0,
          "repeatRatioY": 1.0
        }
      ]
    }
  ]
}
```

#Contact

Please contact me at mattjohns384@gmail.com with any questions or bug reports.

Alternatively you can create a GItHub account and submit an error or suggestion yourself using the 'Issues' tab.  Or I can create
an issue for you if you email me the details of the error.

Please let me know if any of the documentation is unclear and I'll do my best to add more details to it.
