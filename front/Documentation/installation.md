# ETAPE 0
- Aller sur le repertoire Keycloak : cd /eugb/Keycloak
- lancer la commande pour builder l'image de keycloak : docker-compose up 


# ETAPE 1
- Sur le dossier exemple copier le fichier environment-home.ts
- Coller le fichier dans le dossier home/src/app/config
- Renommer le fichier environment-home.ts en environment.ts
- Remplacer urlLogin par l'url de votre serveur keycloak (http://localhost:9080 par exemple)
- Aller sur le dossier home : cd /eugb/front/home
- Si c'est la premiere fois que vous lancer le projet, lancer la commande : npm install
- Lancer la commande :  npm start

# ETAPE 2
- Sur le dossier exemple copier le fichier environment-pats.ts
- Coller le fichier dans le dossier pats/src/app/config
- Renommer le fichier environment-pats.ts en environment.ts
- Sur le dossier exemple copier le fichier keycloak.environment-pats.ts
- Coller le fichier dans le dossier pats/src/app/config
- Renommer le fichier keycloak.environment-pats.ts en keycloak.environment.ts
- sur le fichier keycloak.environment.ts remplacer url par l'url de votre serveur keycloak (http://localhost:9080 par exemple)
- Si c'est la premiere fois que vous lancer le projet, lancer la commande : npm install
- Lancer la commande : npm start



