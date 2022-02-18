package uj.java.kindergarten;

import java.util.concurrent.Semaphore;

public record KidForks(Semaphore first, Semaphore second) {
}
