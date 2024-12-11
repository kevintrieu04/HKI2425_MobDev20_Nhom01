# -*- coding: utf-8 -*-
from themoviedb import TMDb


def main():
    tmdb = TMDb(key="3cfae25bfbc4eb423bf3c15b168982b9")
    results = tmdb.search().multi("arcane")
    for result in results:
        if result.is_movie():
            movie = tmdb.movie(result.id).details()
            print(movie, movie.overview, "\n")
        elif result.is_person():
            person = tmdb.person(result.id).details()
            print(person, person.biography, "\n")
        elif result.is_tv():
            tv = tmdb.tv(result.id).details()
            print(tv, tv.overview, "\n")


main()
