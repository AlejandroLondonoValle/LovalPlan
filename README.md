# LovalPlan · Gestor de horarios gamificado (LovalTech)

App Android para organizar tu semana, recibir **alarmas** antes de cada actividad y ganar **XP, rachas y logros** al cumplirlas.
Misma arquitectura que InventarioApp: **MVVM + Room + Hilt + Navigation 3 + Firestore**, organizada por funcionalidad (`feature/…`) y por capas (`domain`, `data`, `presentation`), con `screen / section / composables / state / viewmodel`.

- Paquete / applicationId: `com.lovaltech.lovalplan`
- Tema: modo oscuro con azul de marca `#005EDC` y el logo del dragón de LovalTech.
- Versiones: las mismas que ya compilaron en tu Android Studio (AGP 9.5.0, Gradle 9.6.0, Kotlin 2.4.20, KSP 2.3.12).

## 1. Abrir y correr

1. Clona el repositorio y en Android Studio: **File → Open → carpeta `LovalPlan`**.
2. Copia **tu** `google-services.json` en `app/` (ver sección 2 y la nota de abajo). Sin ese archivo el plugin de Google Services no deja compilar.
3. Espera el Gradle Sync y corre en un emulador o celular (Android 8.0+).

### ¿Por qué `google-services.json` no está en el repositorio?

El archivo está excluido en `.gitignore` a propósito:

- **Es configuración de un proyecto Firebase concreto**: trae el ID del proyecto, la API key de Android y el cliente OAuth de Google Sign-In. Quien clone el repo debe usar **su propio** proyecto Firebase, con su propio SHA-1 y sus propias reglas.
- **Evita exponer datos del proyecto en un repositorio público**: la API key de Firebase no es una contraseña (la seguridad real la dan las reglas de Firestore y Authentication), pero publicarla facilita abusos de cuota, y GitHub la marca como secreto expuesto.
- **Cada máquina tiene un SHA-1 distinto**: el cliente OAuth depende de la huella del certificado de depuración de cada desarrollador, así que un JSON compartido no le serviría a otro equipo para iniciar sesión con Google.

## 2. Configurar Firebase (recomendado: un proyecto nuevo "LovalPlan")

1. **Crear proyecto** en console.firebase.google.com (sin Analytics).
2. **Agregar app Android** con el paquete exacto `com.lovaltech.lovalplan`.
3. **SHA-1 (solo para Google)**: en Android Studio abre la pestaña **Gradle** (lateral derecho) → `LovalPlan › app › Tasks › android › signingReport` (o en la Terminal: `gradlew signingReport`). Copia el **SHA1** de la variante `debug` y pégalo en *Configuración del proyecto → Tus apps → Agregar huella digital*.
4. **Authentication → Sign-in method**: habilita **Anónimo** y **Google** (elige tu correo de asistencia y guarda).
5. **Descarga `google-services.json` DESPUÉS de los pasos 3 y 4** (así trae el cliente OAuth) y colócalo en `app/google-services.json`.
6. **Firestore Database → Crear** (Standard, modo producción) → pestaña **Reglas** → pega `firestore.rules` → **Publicar**.
7. Sync Project with Gradle Files y vuelve a correr.

> Si "Continuar con Google" dice que no está configurado: faltó el SHA-1 o habilitar Google, o descargaste el JSON antes de hacerlo.

## 3. Pruebas rápidas

- [ ] Crear "Gimnasio" semanal (L-X-V) y "Parcial" de fecha única.
- [ ] Crear una actividad que empiece en 3 minutos con aviso "A la hora" → bloquea el celular → debe sonar la **alarma a pantalla completa**.
- [ ] Al terminar llega "¿Cumpliste…?" → **¡Lo cumplí!** → notificación de +XP.
- [ ] En Inicio, marcar una actividad en curso → **celebración** con XP y racha.
- [ ] Logros: ver nivel, racha de 7 días y medallas.
- [ ] Perfil: cambiar meta diaria, revisar permisos, Sincronizar, vincular Google.
- [ ] Modo avión: todo sigue funcionando; al reconectar, Sincronizar.
- [ ] Pruebas unitarias: clic derecho en `app/src/test` → **Run Tests** (21 pruebas de reglas de negocio).

## 4. Estructura

```
com.lovaltech.lovalplan
├── LovalPlanApplication / MainActivity
├── core/        database · di · navigation · designsystem · sync · util
└── feature/
    ├── auth/       Google + invitado, sesión, cerrar sesión
    ├── schedule/   CRUD de actividades (el "Product" de la fase 1)
    ├── progress/   cumplimientos, XP, niveles, rachas, logros
    ├── dashboard/  pantalla de inicio (combina las demás features)
    ├── profile/    nombre, meta diaria, permisos, cuenta
    └── reminder/   alarmas (AlarmManager), notificaciones, pantalla de alarma
```

La explicación completa (diagramas, flujos, archivo por archivo y preguntas de sustentación) está en el **PDF de sustentación**.
