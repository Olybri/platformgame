# Conception

Ce fichier cite les différentes extensions et modifications apportées au projet par rapport à l'énoncé initial.

## 1. Contrôles personnalisables

Plutôt que de donner à *Player* la responsabilité d'interpréter les contrôles de l'utilisateur, nous avons développé une classe *Command*.

Cette classe, qui ne possède que des méthodes statiques, se charge de lire un fichier de contrôles et de conserver une référence sur un système d'entrées (de type *Input*).

L'initialisation des contrôles se déroule dans la méthode *Program.main*. Le fichier de contrôles chargé se nomme *controls.cfg*.

Il est ensuite possible de connaître l'état d'un bouton associé à une action, à tout moment et à tout endroit, grâce aux méthodes :

- `Command.isButtonDown("action")`
- `Command.isButtonPressed("action")`

Il est également possible de désactiver les entrées afin d'empêcher le joueur de réaliser des actions grâce à `Command.enable(true/false)`.

Cette classe nous offre l'avantage de pouvoir manipuler les actions indépendamment des contrôles définis. Elle permet également à l'utilisateur de pouvoir personnaliser les contrôles en modifiant le fichier *controls.cfg*.

## 2. Comportement du joueur

Certaines extensions et modifications ont été apportées à la classe *Player* afin d'améliorer l'expérience de jeu et l'aspect visuel. Il s'agit notamment des points suivants.

#### a. Interruption des contrôles

Lorsque le joueur apparaît dans le monde, lorsqu'il meurt ou lorsqu'il prend des dégâts, il ne peut plus contrôler son personnage durant un certain délai (ou jusqu'à ce qu'il soit à nouveau en vie).

Cela permet de faire comprendre au joueur qu'il s'agit d'un moment particulier. En effet :

- s'il est mort, il n'est pas censé pouvoir contrôler le personnage ;
- s'il est blessé, il faut qu'il ait la sensation d'être temporairement paralysé afin qu'il s'en rende bien compte ;
- s'il vient d'apparaître, il ne faut pas qu'il parte à toute vitesse alors que le niveau n'est pas parfaitement apparu (à cause du fondu noir en entré).


#### b. Prise de dégâts

Lorsque le joueur prend des dégâts, il est légèrement propulsé dans la direction opposée à sa vitesse et affiche une animation de secousse (réalisée à l'aide de *cooldowns* et de fonction sinusoïdales). Son *sprite* change également temporairement.

Un certain délai de dégât a été introduit afin d'éviter que le joueur ne prenne trop de dégâts en trop peu de temps.

Un comportement semblable a lieu lorsque le joueur meurt, accompagné par une chute du personnage sur le flanc, puis d'un effet de fondu noir, géré par la classe *Fadeout*.

#### c. Faible rotation

Le joueur se penche légèrement dans la direction opposée à sa vitesse, afin d'accentuer visuellement l'effet de vitesse.

## 3. Niveaux stockés dans des fichiers

Afin d'éviter de devoir créer une infinité de sous-classes pour chaque niveau, nous avons développé une classe *DynamicLevel* capable de charger un niveau dynamiquement depuis un fichier. Pour cela, il a fallu définir une syntaxe à respecter, dont la description complète se trouve dans le fichier *README.md*.

Lors de la génération du niveau, un objet  (de type *Object*) est créé à chaque instruction du fichier du niveau. Jusqu'à maintenant, il ne peut s'agir que d'acteurs (*Actor*) ou de signaux (*Signal*). Si l'objet en question est un acteur, il est automatiquement enregistré dans le monde.

Il est possible de conserver des références sur certains objets. Cela permet d'utiliser un objet déjà créé en tant que paramètre pour en construire un autre. Ce stockage de référence se fait grâce à une *HashMap* nommée *variables* qui associe à un objet un identifiant (un symbole) de type *String*. Dès qu'un identifiant est donné pour un certain objet, ce dernier est automatiquement enregistré dans la *HashMap*.

Pour éviter les comportements inattendus, une gestion d'erreurs a été mise en place à l'aide des exceptions afin de cibler facilement les instructions qui n'ont pas pu être interprétées correctement.

## 4. Décor

Pour compléter un peu l'apparence assez vide des niveaux, nous avons développé une classe *Scenery* qui ne fait rien d'autre qu'afficher différents éléments non-solides qui n'interagissent pas. Elle est notamment utilisée pour afficher des arrière-plans.

Il est possible de définir un facteur de taille pour chaque élément, mais également un facteur de distance, qui permet de placer les éléments plus ou moins près des yeux du joueur. On obtient ainsi un effet de perspective assez intéressant.

Un facteur de distance supérieur à zéro indique que l'élément se trouve plus loin que le niveau. Un facteur inférieur à zéro indique qu'il se trouve plus près. Un facteur égal à zéro indique que l'élément se trouve à la même profondeur que le niveau. L'effet de perspective est ensuite simulé en se basant sur *World.getViewCenter*.

Il existe également un acteur *SceneBound* sous-classe de *Scenery*, qui affiche simplement un cadre noir autour de la scène lorsque la fenêtre est trop redimensionnée. Cela permet d'éviter que l'utilisateur voie « trop loin », ce qui afficherait des bouts de décors coupés et donc non souhaitables.

## 5. Particules

Afin d'améliorer l'aspect visuel du jeu, nous avons mis au point une classe abstraite *Particle* capable d'afficher une courte animation avant de s'autodétruire.

Il n'existe pour l'instant qu'une seule forme de particule utilisée en jeu, nommée *Smoke*, qui représente une boule de fumée qui se dissout rapidement dans l'air. Cette particule est notamment utilisée lorsque : le joueur souffle, une porte change d'état, une boule de feu disparaît, un ennemi meurt, etc.

## 6. Animation

Nous avons développé une class *Animation* qui a pour seule responsabilité de donner un nom de sprite en fonction du temps. Elle permet aux différents acteurs animés de pouvoir afficher aisément la bonne image.

L'animation peut soit tourner en boucle, soit n'être jouée qu'une seule fois.

Son principal intérêt est de mieux séparer les responsabilités et d'éviter la répétition de code.

## 7. Ennemis

Nous avons élaboré une classe abstraite *Enemy* qui décrit un ennemi faisant des allers-retours entre un point et sa position initiale. S'il touche le joueur, il le blesse. S'il se fait toucher par une boule de feu, il meurt.

L'ennemi ne subit pas de contrainte physique (gravité et collision), afin de pouvoir imaginer toute sorte de créature (par exemple une scie qui parcourt un mur, un ennemi qui sort d'un mur, etc.).

Il n'existe pour l'instant qu'une seule forme d'ennemi, nommée *Slime*, qui représente une créature rose visqueuse qui rampe au sol.

## 8. Anti-joueur

Il existe également un ennemi « spécial », immortel et insensible aux collisions, qui n'est pas une sous-classe d'*Enemy* à cause de ses caractéristiques particulières. Il s'agit de la classe *AntiPlayer*, un personnage semblable au joueur qui suit exactement le même parcours que ce dernier, avec un certain délai. S'il touche le joueur, il lui inflige d'importants dégâts.

Cela ajoute un concept de gameplay intéressant qui force le joueur à ne pas revenir sur ses pas, afin de ne pas rencontrer l'anti-joueur.

Cette classe manipule une *HashMap*, nommée *positions*, qui associe à chaque position une valeur de temps. Lors de la mise à jour de cet acteur, on parcourt toutes les positions et on récupère celle qui correspond au temps actuel soustrait d'un certain délai. On ajoute également, à chaque mise à jour, la position actuelle du joueur dans la liste, et on supprime toutes celles qui sont devenues obsolètes.

L'utilisation d'une *HashMap*, plutôt qu'une simple liste, permet notamment d'éviter une éventuelle instabilité dans le déplacement de l'anti-joueur due à une chute de frames par seconde.

## 9. Pentes

Afin de varier la forme du terrain, nous avons développé une classe *Hill* qui représente un élément pentu sur lequel le joueur peut marcher. Pour gérer les collisions, la méthode *Hill.getBox* renvoie une boîte de collision dont la hauteur est calculée en fonction de la position horizontale de l'acteur qui veut interagit avec la pente.

Afin d'éviter une collision avec un bloc solide en fin de pente, il est préférable de la faire suivre d'un bloc de type *Platform*. Il s'agit en effet d'une classe qui représente un bloc qui n'est solide que pour les acteurs qui le percute depuis le dessus.

## 10. Animations diverses

De nombreux éléments de jeux sont légèrement animés afin de rendre l'aspect visuel un peu plus vivant. On peut notamment citer :

- les cœurs et les clés qui flottent horizontalement dans l'air ;
- les cœurs au-dessus du joueur (classe *Overlay*) qui gonflent lorsque celui-ci récupère un cœur, et qui sont secoués lorsque sa santé est faible ;
- les ennemis qui meurent sont secoués, d'une manière semblable au joueur qui prend des dégâts.

La plupart de ces animations profitent de fonctions sinusoïdales afin de simuler un mouvement fluide et répétitif.

## 11. Couleurs

Certains éléments existent dans plusieurs couleurs. Afin de construire un de ces acteurs avec la couleur désirée, nous avons créé une classe *ItemColor*, représentant une couleur, qui ne fait rien d'autre que manipuler une chaîne de caractère décrivant la couleur.

Nous avons donc la possibilité de choisir parmi les différentes couleurs qui existent grâce aux instances statiques déjà définies, comme `ItemColor.RED`, `ItemColor.BLUE`, etc.