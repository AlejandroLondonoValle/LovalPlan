# LovalPlan · Gestor de horarios gamificado (LovalTech)

App Android para organizar tu semana, recibir **alarmas** antes de cada actividad y ganar **XP, rachas y logros** al cumplirlas.
Misma arquitectura que InventarioApp: **MVVM + Room + Hilt + Navigation 3 + Firestore**, organizada por funcionalidad (`feature/…`) y por capas (`domain`, `data`, `presentation`), con `screen / section / composables / state / viewmodel`.

- Paquete / applicationId: `com.lovaltech.lovalplan`
- Tema: modo oscuro con azul de marca `#005EDC` y el logo del dragón de LovalTech.
- Versiones: las mismas que ya compilaron en tu Android Studio (AGP 9.5.0, Gradle 9.6.0, Kotlin 2.4.20, KSP 2.3.12).

## 1. Abrir y correr (sin Firebase todavía)

1. Descomprime y en Android Studio: **File → Open → carpeta `LovalPlan`**. Espera el Gradle Sync.
2. Corre en un emulador o celular (Android 8.0+). Entra como **invitado**.
3. Con el `google-services.json` de relleno todo funciona en local (horario, alarmas, XP, logros). Sincronizar dirá "Sin conexión".

## 2. Configurar Firebase (recomendado: un proyecto nuevo "LovalPlan")

1. **Crear proyecto** en console.firebase.google.com (sin Analytics).
2. **Agregar app Android** con el paquete exacto `com.lovaltech.lovalplan`.
3. **SHA-1 (solo para Google)**: en Android Studio abre la pestaña **Gradle** (lateral derecho) → `LovalPlan › app › Tasks › android › signingReport` (o en la Terminal: `gradlew signingReport`). Copia el **SHA1** de la variante `debug` y pégalo en *Configuración del proyecto → Tus apps → Agregar huella digital*.
4. **Authentication → Sign-in method**: habilita **Anónimo** y **Google** (elige tu correo de asistencia y guarda).
5. **Descarga `google-services.json` DESPUÉS de los pasos 3 y 4** (así trae el cliente OAuth) y reemplaza `app/google-services.json`.
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
