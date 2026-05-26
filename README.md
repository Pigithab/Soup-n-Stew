# 🍲 SOUP & STEW 🍲
#### A 1.21.1 Minecraft Forge mod that puts minecraft's food mechanics to shame!    
Since minecraft won't give us a Food Update, with the latest food added being the glowberry, I decided to make my own.
In this mod soup/stews are no longer craftable via the old and boring crafting table,
but instead, are now obtainable via the cooking cauldron, which takes ingredients fuel and water to produce sweet delicious soup.
## Features
### Cooking Cauldron
![Cooking Cauldron](screenshots/Cooking%20Cauldron.png)
The Cooking Cauldron is designed to make soup/stew with a simple cooking process
### Soups / Stews
![All Stews](screenshots/All%20Recipes.png)
This mod adds 7 new unique stews with their own recipe and textures including:
- Fish Stew
- Beef Stew
- Pork Stew
- Rotten Stew
- Chicken Stew
- Pumpkin Soup
- Vegetable Stew

And disables the vanilla stew/soups crafting recipe, now only obtainable via the Cooking Cauldron.
### Custom Creative Tab
![Custom Creative Tab](screenshots/Creative%20Tab.png)
The Creative Tab contains the basic items needed for cooking, all soup/stews and all ingredients required for every recipe.
## The Process
### Cauldron Recipe
The Cooking Cauldron can be crafted with 7 cobblestone blocks arranged like this:
![Cooking Cauldron Recipe](screenshots/Cooking%20Cauldron%20Recipe.png)
### GUI
It has 3 ingredient slots and 1 fuel slot, when a valid recipe is inserted and has Water and Fuel the soup/stew will begin cooking.
![Cooking Cauldron GUI](screenshots/Cooking%20Cauldron%20GUI.png)
The Fuel Tank takes an item from the fuel slot every second ( 20 game ticks) and adds it to the total fuel,
the tank will only consume fuel if there is enough space to avoid waste.
### Animation & Particles
During the cooking process the cooking cauldron starts producing smoke and splash particles.
![Cooking Cauldron Animation](screenshots/Cooking%20Animation.png)
### Damage
Be Careful though because during this time the cauldron deals damage to any entities inside of it.
![Boiled Chicken](screenshots/Boiling%20Chicken.png)
### 2 Custom Death Messages
![Death Message 1](screenshots/Death%20Message%201.png)
![Death Message 2](screenshots/Death%20Message%202.png)
### Finished Soup/Stew
When the process is finished the water switches colours depending on the type of soup/stew.
![Cauldron with Stew](screenshots/Cauldron%20with%20Stew.png)
### Collecting your Soup/Stew
Right-clicking with a bowl will give the player a bowl of the stew and lower the water level by 1, the Cauldron can hold up to 3 layers of water / stew.
![Cauldron -1](screenshots/Cauldron%20-1.png)
Soup/stew can be cleared at any level by right-clicking with a Water Bucket to rinse it.
## How to Use
### with .jar file
#### 1. Download the mods .jar file
#### 2. Download Forge for Minecraft version 1.21.1
#### 3. Add .jar file to your minecraft mods folder
#### 4. Launch the game and enjoy!
### with Gradle
#### 1. Clone this repository
   ```bash
   git clone https://github.com/Pigithab/Soup-n-Stew
   ```
#### 2. Navigate to the folder
   ```bash
   cd Soup-n-Stew
   ```
#### 3. runClient
   ```bash
   gradlew runClient
   ```