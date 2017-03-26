# Rendu TP Akka Actors
Réalisé par Jonathan Lecointe et Louis Guilbert

26/03/2017

---

## Introduction
Ce logiciel utilise la librairie akka afin de diffuser un message dans un graphe.
Il crée deux systèmes distants et une console locale afin de créer les noeuds d'un graphe, définir les liaisons du graphe et envoie un message à un noeud du graphe.
Ce message est alors transmis à tous les autres noeuds du graphe de voisin en voisin.

### Executer le code :
Chaque système est à lancer sur un terminal différent.
La console doit être lancée après les 2 autres systèmes.
```
sbt
> run sys1
```

```
sbt
> run sys2
```

```
sbt
> run console
```

### Executer les tests : 
```
sbt
> test
```

## Architecture

### Général
Nous avons réalisé un main qui crée le système demandé en paramètre.
Le système 1 est créé par le paramètre sys1 et crée les noeuds 0, 1 et 2.
Le système 2 est créé par le paramètre sys1 et crée les noeuds 3, 4 et 5.
La console est créé par le paramètre consolee et crée les liens entre les noeuds via des messages.
Le graphe resemble alors à ceci : 

![Graphe représenté](img/graph.PNG)

### Code Samples
```
case TextMessage(res) =>
	if(!visited) {				
		println("Node " + id + " valeur : " + res)
		neighbours.map { n =>
		  context.actorSelection(n) ! TextMessage(res + 1)
		}
		visited = true
	}
```
Pour éviter la boucle infinie due à la propagation des messages, nous utilisons un boolean passé à true lorsqu'un message est reçu.
Cela ne permet pas d'envoyer plusieurs messages. Un autre système utilisant un identifiant dans le message envoyé aurait pu permettre l'envoi de plusieurs message, mais la console n'étant pas intéractive, nous n'avons pas trouvé cela intéressant d'implémenter un tel système.
