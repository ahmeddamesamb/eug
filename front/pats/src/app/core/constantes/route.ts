export const routes = {

  home: '/home',
  children: {
    dashboard : {

      route : "dashboard",
      children : {}
    },

    gir :{
      route: "gir",
      children : {
        parametrage : {
          route: "parametrage",
          children : {
            formation : {
              route: "formation",
              children : {}
            },

            specialite:{
              route: "specialite",
              children : {}
            },

            mention : {
              route: "mention",
              children : {}
            },

            domaine : {
              route: "domaine",
              children : {}
            },

            ufr : {
              route: "ufr",
              children : {}
            },

            universite : {
              route: "universite",
              children : {}
            },

            ministere : {
              route: "ministere",
              children : {

              }
            }


          }
        }


      },

    }

    // others pages here


  },



};
