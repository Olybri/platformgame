# PlatformGame

Mini-projet (2) 2016-17 : Jeux de plate-formes

http://proginsc.epfl.ch/wwwhiver/mini-projet2/descriptif.html

Par Loris Witschard et Soulaymane Lamrani

## Utilisation

Main class : `platform.Program`

Pour améliorer les performances, il est possible de lancer le jeu avec l'option `-Dsun.java2d.opengl=true` afin de profiter de l'accélération matérielle.

#### Contrôles

Les contrôles peuvent être définis dans le fichier *controls.cfg* sous la forme `action: contrôle`.

Contrôles par défaut :

| Action | Contrôle |
| --- | --- |
| Se déplacer à droite | Flèche droite |
| Se déplacer à gauche | Flèche gauche |
| Sauter | Flèche haut |
| Lancer une boule de feu | Espace |
| Activer | E |
| Souffler | B |

#### Déroulement d'une partie

- En lançant une boule de feu, le joueur peut allumer une **torche** ou tuer un **ennemi**.
- En soufflant, le joueur peut éteindre une **torche**.
- Le joueur peut activer les **leviers**, qui se désactivent automatiquement au bout de 10 secondes.
- Le joueur peut récupérer des **clés** et des **cœurs** en se déplaçant dessus.
- Les **clés**, **leviers** et **torches** permettent de déverrouiller des portes ou déplacer des plateformes.
- Le joueur possède initialement (et au maximum) **5 points de vie**. Il en perd lorsqu'il entre en contact avec un ennemi ou des piques, et en gagne lorsqu'il récupère un cœur.
- Le joueur meurt s'il tombe dans le **vide**, ou s'il se fait écraser dans un mur.
- Dans certains niveaux, on peut trouver un **anti-joueur** qui se comporte comme un fantôme suivant (avec un certain délai) exactement le même parcours que le joueur, immortel et insensible aux collisions. Il procure des dégâts importants au joueur s'ils entrent en collision.

#### But du jeu

Atteindre la porte de sortie de chaque niveau.

## Niveaux

### Solution

#### Niveau 1

- Aller sur le *mover* (plateforme en bois) et lancer une boule de feu sur la torche pour le faire monter.
- Tuer le *slime* et récupérer la clé bleue pour faire disparaître les verrous bleus.
- Descendre et éteindre la torche pour faire disparaître les verrous rouges.
- Traverser la fosse de piques en sautant.
- Allumer la torche à distance pour faire réapparaître les verrous rouges.
- Sauter jusqu'au levier et l'activer pour faire disparaître les verrous verts.
- Revenir au départ en passant par le haut.
- Utiliser le *jumper* pour atteindre la sortie, avant que les verrous verts ne réapparaissent.

### Création de niveaux

Les différents niveaux sont stockés dans des fichiers situés dans le dossier *lvl*.

Un fichier de niveau respecte la syntaxe suivante :

- Une seule instruction par ligne.
- Les instructions sont insensibles à la casse.
- Les lignes vides ou ne commençant pas par un caractère alphabétique sont ignorées.
- Les acteurs construits sont automatiquement enregistrés dans le monde.
- Un vecteur *(x; y)* est représenté sous la forme `x:y`, sans espace autour des doubles points.
- Une boîte *(x; y; w; h)*, définie par son centre et sa taille, est représentée sous la forme `x:y:w:h`, sans espace autour des doubles points.
- Les chaînes de caractères sont représentées sans guillemets.
- Un nom de référence ne peut être utilisé qu'une seule fois.

#### Instructions

| Instruction | Définition |
| --- | --- |
| `class_name param_1 param_2 ...` | Construit un objet (acteur ou signal). |
| `ref = class_name param_1 param_2 ...` | Construit un objet et garde une référence `ref` sur ce dernier.
| `class_name ref param_2 param_3 ...` | Construit un objet en passant une référence en paramètre. |

#### Exemple

```
// Titre, auteur...
player 0:3
block 0:2:3:1 solid.block
sig_torch = torch 2:4 false
not_torch = not sig_torch
door 9:4 red not_torch
exit 1:8 level_2.txt true
```

#### Erreurs

En cas d'erreur dans un fichier de niveau, la ligne concernée est ignorée et le problème est signalé dans la sortie standard. Exemple :

```
Level file: level_1.txt | Ignored line 138 | Cannot convert string '15:5' to class platform.util.Box.
Level file: level_1.txt | Ignored line 250 | Too few arguments to construct object 'mover'.
```
