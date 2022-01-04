# Koffie services #
## Beschrijving ##
Ons thema voor het eindproject is: __Koffie__.\
Hierbij hebben we 3 microservices gemaakt.

Naam |Service|Github Repo
-----|-------|-----------
Niels Stinissen|GerechtenService|[Repo Link](https://github.com/nielsstinissen/APT_koffie_gerechten/ "GerechtenService")
Youri Seijkens|KoffieService|[Repo Link](https://github.com/yseij/Koffie_Dranken "KoffieService")
Arnaud Provoost|BoonService|[Repo Link](https://github.com/ArnaudProvoost/APT_coffee_Bonen "BoonService")

In onze service kan je dus informatie vinden over verschillende koffiebonen in de __BoonService__.\
In de __KoffieService__ kan je verschillende soorten koffie vinden.\
En bij de __GerechtenService__ vind je gerechten terug die een bepaalde koffie hebben als ingredient.

## Front End ##
Ook hebben we een kleine Angular front end applicatie gemaakt waarop we hebben gelinked met de edge service\
[Front End Angular](https://apt.sinners.be/ "Front End Angular")\
[Repo Link](https://github.com/yseij/front-end-koffie "FrontEndRepo")

## Deployment Diagram ##
![deployment-diagram](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/deployment-diagram.jpg)

## Swagger UI ##
[Swagger pagina] (https://koffie-edge-service-server-arnaudprovoost.cloud.okteto.net/swagger-ui.html "Swagger pagina")\
Beginpagina\
![begin-pagina](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/swagger-index.jpg) \
GET Alle koffie\
![get-alle-koffie](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/swagger-allekoffie.jpg) \
GET Alle koffie op soort en naam\
![get-alle-koffie-soort-name](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/swagger-allekoffie-soort-name.jpg) \
POST Gerecht aanmaken\
![post-gerechten](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/swagger-gerechten-post.jpg) 

## Postman ##
GET /\
![get-index](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-index.jpg) \
GET /AlleKoffie\
![get-allekoffie](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-allekoffie.jpg) \
GET /AlleKoffie/naam={naam}\
![get-allekoffie-naam](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-allekoffie-naam.jpg) \
GET /AlleKoffie/soort={soort}/name={name}\
![get-allekoffie-soort-name](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-allekoffie-soort-name.jpg) \
GET /boons/land/{land}\
![get-boons-land](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-boons-land.jpg) \
GET /boons/naam/{naam}\
![get-boons-naam](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-boons-naam.jpg) \
POST /gerechten\
![post-gerechten](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-gerechten-post.jpg) \
GET /gerechten/afkomst/{afkomst}\
![get-gerechten-afkomst](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-gerechten-afkomst.jpg) \
GET /gerechten/koffiedrank/{koffieDrankNaam}\
![get-gerechten-koffiedrank](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-gerechten-koffiedrank.jpg) \
DELETE /gerechten/naam/{naam}\
![delete-gerechten](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-gerechten-delete.jpg) \
PUT /gerechten/naam/{naam}\
![put-gerechten](https://github.com/nielsstinissen/APT_koffie_edgeservice/blob/main/screenshots/postman-gerechten-put.jpg) 
