# Tank Software Design Platformer Game

В `main` собрана итоговая версия проекта из веток `hometask-1`–`hometask-8`
и `final`. Учебные ветки сохраняют историю отдельных этапов.

## Архитектура проекта

Проект следует принципам **Луковой архитектуры (Onion Architecture)**:

```plaintext
+---------------------------------------------------------------+
|                            Config                             |
|    (GameDesktopLauncher, GameConfig, GraphicsConfigurator)    |
+-------------------------------+-------------------------------+
                                |
                                v
+-------------------------------+-------------------------------+
|                        Infrastructure                         |
| (CommandsFactoryImpl, LevelGeneratorImpl, WorldRenderer, ...) |
+-------------------------------+-------------------------------+
                                |
                                v
+-------------------------------+-------------------------------+
|                          Application                          |
|       (GameLoop, CommandsFactory, LevelGenerator, ...)        |
+-------------------------------+-------------------------------+
                                |
                                v
+-------------------------------+-------------------------------+
|                            Domain                             |
|          (GameRules, GameObject, GameCharacter, ...)          |
+---------------------------------------------------------------+
```

- **Domain** (`ru.mipt.bit.platformer.domain`): Cущность (главная логика игры). Определяет основные сущности (`GameCharacter`, `GameObject`) и правила игры (`GameRules`). Абстракции, независим от деталей
- **Application** (`ru.mipt.bit.platformer.application`): Варинаты использования. Зависит от Domain
- **Infrastructure** (`ru.mipt.bit.platformer.infrastructure`): Инфра, уровень адаптеров, презентаторов и контроллеров. Заивисит от Application
- **Config** (`ru.mipt.bit.platformer.config`): Конфигураци, фреймфорк. Собирает приложение воедино, внедряя зависимости (Spring Context, `GameConfig`, `GraphicsConfigurator`). Точка входа в приложение

## Требования

- JDK 17 (версия, использованная при проверке)
- Gradle 8.5 через включённый в репозиторий `./gradlew`; отдельная установка Gradle не нужна
- macOS (поддержка других платформ через соответствующий бэкенд LibGDX)

```bash
brew install openjdk@17
java --version
```

## Сборка проекта

### Без тестов

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean build -x test
```

### Сборка с тестами

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean build
```

## Запуск игры

```bash
./gradlew run
```

Или с опцией --no-daemon:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew run --no-daemon
```

## Тестирование

### Запуск всех тестов

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew test
```

### Просмотр отчета о тестах

```bash
open build/reports/tests/test/index.html
```

### Запуск конкретного теста

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew test --tests "ru.mipt.bit.platformer.domain.entities.DirectionTest"
```

## Управление танком

- **W** или ↑ - движение вверх
- **S** или ↓ - движение вниз
- **A** или ← - движение влево
- **D** или → - движение вправо
- **SPACE** - выстрел

## Конфигурация карты

Карта настраивается в файле `src/main/resources/mapLayout.txt`:

- **X** - стартовая позиция танка
- **T** - дерево (препятствие)
- **_** - пустая клетка

## Генерация уровней

Режим задаётся в методе `mapLayout()` класса `config/GameConfig.java`:

### Режим 1: Случайная генерация

```java
mapLayout = new MapLayout(groundLayer);
```

### Режим 2: Загрузка из файла

```java
mapLayout = new MapLayout("mapLayout.txt");
```

## Создание дистрибутива

### JAR файл

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew jar
```

JAR файл будет находиться в: `build/libs/software-design-platformer-1.0-SNAPSHOT.jar`

Этот JAR содержит классы проекта. Для запуска приложения вместе с зависимостями
используйте дистрибутив, созданный плагином Gradle Application.

### Запуск собранного приложения

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew installDist
./build/install/software-design-platformer/bin/software-design-platformer
```

### Архивы дистрибутива

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew distZip distTar
```

Архивы будут в: `build/distributions/`

## Полезные команды

### Очистка

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean
```

### Остановка Gradle daemon

```bash
./gradlew --stop
```

### Запуск без daemon

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew run --no-daemon
```

### Просмотр задач

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew tasks
```
