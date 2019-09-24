package ru.otus.cinemaapp.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.model.Film;

public class FilmRepository implements FilmRepositoryInt {

    private List<Film> films = new ArrayList<>();

    private static FilmRepository instance;

    private FilmRepository() {
        films.add(new Film(
                Film.generateId(),
                "Однажды в голливуде",
                "Фильм повествует о череде событий, произошедших в Голливуде в 1969 году, " +
                        "на закате его «золотого века». Известный актер Рик Далтон и его дублер " +
                        "Клифф Бут пытаются найти свое место в стремительно меняющемся мире " +
                        "киноиндустрии.",
                R.drawable.hollywood));
        films.add(new Film(
                Film.generateId(),
                "Интерстеллар",
                "Когда засуха приводит человечество к продовольственному кризису, " +
                        "коллектив исследователей и учёных отправляется сквозь червоточину " +
                        "(которая предположительно соединяет области пространства-времени через " +
                        "большое расстояние) в путешествие, чтобы превзойти прежние ограничения " +
                        "для космических путешествий человека и переселить человечество " +
                        "на другую планету.",
                R.drawable.interstellar));
        films.add(new Film(
                Film.generateId(),
                "Щегол",
                "История юного Теодора Деккера, потерявшего мать во время теракта в " +
                        "Метрополитен-музее. Чудом оставшись в живых после взрыва, Тео получает от " +
                        "умирающего старика редкую картину кисти Карела Фабрициуса и кольцо. " +
                        "С этого момента начинается его погружение в подпольный мир искусства.",
                R.drawable.goldfinch));
    }

    public static FilmRepository getInstance() {
        if (instance == null) {
            instance = new FilmRepository();
        }
        return instance;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return films
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Film> getFilmList() {
        return films;
    }

    @Override
    public void saveFilm(Film film) {
        Objects.nonNull(film);
        films.add(film);
    }
}
