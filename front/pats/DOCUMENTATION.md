# PATS
L'application angular doit tourner sur le port 4201 
# Sur votre interface d'administration de keycloak
- Se connecter sur keyclaok en tant que administrateur
- Créer un realm nommé pats
- Créer un client nommé e-ugb
- Pour le client e-ugb:
    - ROOT URL: http://localhost:4201
    - HOME URL: http://localhost:4201
    - VALID REDIRECT URI: http://localhost:4201/*
    - VALID REDIRECT LOGOUT URI: http://localhost:4200/*
    - WEB ORIGIN: *
    - SUR Login setting: choisir le theme themePATS

- Créer un utilisateur pats (username:pats, password: pats, email, prénom, nom)










