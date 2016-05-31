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

## Usage
This mod consists of a 3 parts: The mod itself, the configuration file, and the images for each page of the book.

The mod is located at https://github.com/MattJohns/ImageBook/blob/master/imagebook-0.1.jar .  This should go in the mods folder
of your minecraft directory, along with the other mods for your mod pack.

The configuration file 'imagebook.json' should go in the config folder of your minecraft directory.  Please see the configuration section
below for the format of the file.

Images for the pages of the book should go in a sub folder within your config folder.  They can be placed elsewhere but this
is the recommended location.  The sub folder should be named after the content of the book, for example 'MagicRecipies3'.

Once those are in place the mod should automatically load when you run the game.

## Error Log
If the book is not loading, please check the error log for any messages.  If there is an image missing the book will not load and will
not display an error to the user in-game.  It will only write error messages to the log which can be found in your logs folder in the minecraft
directory, under the name 'fml-client-latest.log' .

Search for any messages from 'ImageBook' within that file to find where the error is occuring.  Please contact me if you are not able to
understand the error message.

## Configuration
The configuration file is more complicated than most other mods as it needs to hold multiple books with multiple pages in each book, as
well as information about margins and the book icon itself.  The format is also different than other mods (.json) because the standard
forge configuration files are not able to contain nested arrays.

Each field of the configuration file is described below.  As I understand the format is complicated I'll do my best to expand on these sections
until they are as clear as possible.  If there are any misunderstandings please contact me and I'll update the appropriate section with
more information.

### General Structure
This mod may contain multiple books.  The root element "bookList" contains all the information about each book including the pages
for the books.  Because "bookList" is an array it must have all of the books within it contained within square brackets ('[' and ']').

Each book is wrapped by curly braces ('{' and '}') and terminated with a comma.  The format is:

"bookList" : [
	{book1},
	{book2},
	{book3}
]

Note the final book should not be terminated by a comma (as per JSON formatting specifications).

Each book also contains fields that describe the book, such as name, icon, and an array of page images.  These fields are described
below.

### Description
The description field simply describes the configuration file.  This field does not need to be changed and can be ignored.

Example:

{
	"description" : "Configuration file for imagebook mod."
}

### Help URL
The helpUrl field is a link to a page that describes the format of the configuration file.  This field does not need to be changed and
can be ignored.

Example:

{
	"helpUrl" : "https://github.com/MattJohns/ImageBook"
}

### Configuration Version
The configurationVersion field sets the version of the configuration file.  This allows the mod to load older configurations correctly.

Example:

{
	"configurationVersion" : "0.1"
}

### Resource Packs
The resourcePackList field is an array which holds a list of resource packs that contain images for the books.  This field is currently not used
but will be implemented in future versions.

### Book List
The bookList field is an array which holds all of the books and their information such as page images, margin sizes and the book icon to display
for the user.

Example:

{
	"bookList" : [
	]
}

### Internal Name
The internalName field contains the name for the book used internally by the mod.  This should simply be set to the same as the displayName
field.  The reason this field exists is to allow for multiple books that have the same name.  In that situation the internal name should be set to
something unique so the mod can tell the difference between the books with the same display name.

Example:

{
	"bookList" : [
		"nameInternal" : "Example Book"
	]
}

###Display Name


 




