=====================================================
  Ejercicio33_CRUDCompleto - INSTRUCCIONES DE IMPORTACIÓN
=====================================================

1. IMPORTAR EN ANDROID STUDIO:
   File > Open > seleccionar la carpeta "Ejercicio33_CRUDCompleto"

2. GOOGLE-SERVICES.JSON (OBLIGATORIO):
   - Ve a https://console.firebase.google.com
   - Selecciona (o crea) tu proyecto Firebase
   - En Configuración del proyecto > Aplicaciones Android > 
     descarga el archivo google-services.json
   - Cópialo en la carpeta: Ejercicio33_CRUDCompleto/app/
   - Asegúrate de que el package name coincida con: com.lab08.ejercicio33

3. SINCRONIZAR GRADLE:
   - Una vez copiado google-services.json: File > Sync Project with Gradle Files

4. REGLAS DE SEGURIDAD EN FIREBASE:
   - En la consola Firebase, ve a Realtime Database > Reglas
   - Asegúrate de estar en modo de prueba (válido 30 días):
     {
       "rules": {
         ".read": true,
         ".write": true
       }
     }

5. SDK LOCAL:
   - Edita local.properties y corrige la ruta de sdk.dir
   - Android Studio suele hacer esto automáticamente al abrir el proyecto
