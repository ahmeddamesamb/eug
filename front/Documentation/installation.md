# ETAPE 0
- Aller sur le repertoire Keycloak : cd /eugb/Keycloak
- lancer la commande pour builder l'image de keycloak : docker-compose up 


# ETAPE 1
- Sur le dossier exemple/home copier les fichier environment.ts et environment.development.ts
- Coller ces fichiers dans le dossier home/src/envrionments/
- Remplacer urlLogin par l'url de votre serveur keycloak (http://localhost:9080 par exemple)
- Aller sur le dossier home : cd /eugb/front/home
- Si c'est la premiere fois que vous lancer le projet, lancer la commande : npm install
- Lancer la commande :  npm start

# ETAPE 2
- Sur le dossier exemple/pats copier les fichier environment.ts et environment.development.ts
- Coller ces fichiers dans le dossier pats/src/environments/
- Sur ces fichiers  remplacer url par l'url de votre serveur keycloak (http://localhost:9080 par exemple)
- Si c'est la premiere fois que vous lancer le projet, lancer la commande : npm install
- Lancer la commande : npm start



