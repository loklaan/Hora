# Hora

Simple background application to keep you mindful of elapsing time.

Your screen will flash every hour for a brief but noticiable moment; for those that get so caught up in their work, that the hours go by without them noticing.

##Requirements:

* Java
* Windows / OSX / Linux (must supports Java's *system tray*)

## Install
    
Compile:

    javac -d ./ src/*java

Package:

    jar cvfe Hora.jar hora.Application hora/*.class* ./res

## Usage

Run `Hora.jar` by double clicking or typing `java -jar Hora.jar` in terminal. Pause/resume or exit Hora via system tray icon.

### Credit
Ramon Millsteed (developer) twitter`@ramonmillsteed`

### Legal

GNU General Public License, version 3 (GPL-3.0)  
http://opensource.org/licenses/GPL-3.0

Copyright (C) 2014 Ramon Millsteed  
Copyright (C) 2014 Lochlan Bunn
