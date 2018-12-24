java -cp "bin:$(find res/dependencies -name "*.jar" | paste -sd ":" -)" $1
