# Tank Software Design Platformer Game

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

- **Domain** (`ru.mipt.bit.platformer.domain`): Центральный слой. Определяет основные сущности (`GameCharacter`, `GameObject`) и правила игры (`GameRules`). Полностью независим от фреймворков и деталей реализации
- **Application** (`ru.mipt.bit.platformer.application`): Слой логики приложения. Координирует игровой процесс через `GameLoop`, определяет интерфейсы для генерации уровней (`LevelGenerator`) и фабрики команд (`CommandsFactory`). Зависит только от Domain
- **Infrastructure** (`ru.mipt.bit.platformer.infrastructure`): Слой инфраструктуры. Реализует интерфейсы Application (например, `CommandsFactoryImpl`, `LevelGeneratorImpl`) и отвечает за взаимодействие с внешним миром (рендеринг через `WorldRenderer`, ввод пользователя)
- **Config** (`ru.mipt.bit.platformer.config`): Слой конфигурации. Собирает приложение воедино, внедряя зависимости (Spring Context, `GameConfig`, `GraphicsConfigurator`). Точка входа в приложение

## Требования

- Java 17 или выше
- Gradle 8.5+
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
./gradlew test --tests "ru.mipt.bit.platformer.classes.DirectionTest"
```

## Управление танком

- **W** или ↑ - движение вверх
- **S** или ↓ - движение вниз  
- **A** или ← - движение влево
- **D** или → - движение вправо
- **SPACE** - выстрел (пока не было в дз)

## Конфигурация карты

Карта настраивается в файле `src/main/resources/mapLayout.txt`:

- **X** - стартовая позиция танка
- **T** - дерево (препятствие)
- **_** - пустая клетка

## Генерация уровней

В `GameDesktopLauncher.java` доступны два режима:

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

### Запуск JAR

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
java -jar build/libs/software-design-platformer-1.0-SNAPSHOT.jar
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
