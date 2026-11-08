#!/bin/bash

set -e

# Папки
SRC_DIR="src/main/java"
BUILD_DIR="build"
CLASSES_DIR="$BUILD_DIR/classes"
DOCS_DIR="$BUILD_DIR/javadoc"
JAR_FILE="$BUILD_DIR/heap-sort.jar"

# Очистка предыдущей сборки
rm -rf "$BUILD_DIR"

# Создание необходимых папок
mkdir -p "$CLASSES_DIR"
mkdir -p "$DOCS_DIR"

echo "Компиляция исходников..."

# Компиляция Java-файлов
javac -d "$CLASSES_DIR" $(find "$SRC_DIR" -name "*.java")

echo "Генерация документации..."

# Генерация Javadoc
javadoc \
    -d "$DOCS_DIR" \
    -sourcepath "$SRC_DIR" \
    -subpackages heapsort

echo "Создание JAR-файла..."

# Создание JAR
jar \
    --create \
    --file "$JAR_FILE" \
    --main-class heapsort.Main \
    -C "$CLASSES_DIR" .

echo "Запуск приложения..."

# Запуск приложения
java -jar "$JAR_FILE"

echo "Готово!"