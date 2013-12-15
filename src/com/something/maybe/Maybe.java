package com.something.maybe;

public abstract class Maybe<T> {
    public abstract boolean isKnown();
    public abstract T otherwise(T defaultValue);
    public abstract Maybe<T> otherwise(Maybe<T> maybeDefaultValue);
    public abstract <U> Maybe<U> apply(Function<? super T, ? extends U> mapping);
    public abstract void apply(Procedure<? super T> proc);
    public abstract <U> U to(U defaultValue, Function<? super T, ? extends U> mapping);
    public abstract T extract();

    public static <T> Maybe<T> unknown() {
        return new Unknow<T>();
    }

    public static <T> Maybe<T> definitely(final T theValue) {
        return new DefiniteValue<T>(theValue);
    }

    public static <T> Maybe<T> make(T e) {
        return e == null ? Maybe.<T>unknown() : Maybe.<T>definitely(e);
    }

    public static class Unknow<T> extends Maybe<T> {
        @Override
        public boolean isKnown() {
            return false;
        }

        @Override
        public T otherwise(T defaultValue) {
            return defaultValue;
        }

        @Override
        public Maybe<T> otherwise(Maybe<T> maybeDefaultValue) {
            return maybeDefaultValue;
        }

        @Override
        public <U> Maybe<U> apply(Function<? super T, ? extends U> mapping) {
            return unknown();
        }

        @Override
        public void apply(Procedure<? super T> proc) {
        }

        @Override
        public <U> U to(U defaultValue, Function<? super T, ? extends U> mapping) {
            return defaultValue;
        }

        public T extract() {
            throw new RuntimeException("Cannot extract an unknown value");
        }

        @Override
        public String toString() {
            return "unknown";
        }

        @Override
        @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    public static class DefiniteValue<T> extends Maybe<T> {
        private final T theValue;

        public DefiniteValue(T theValue) {
            this.theValue = theValue;
        }

        @Override
        public boolean isKnown() {
            return true;
        }

        @Override
        public T otherwise(T defaultValue) {
            return theValue;
        }

        @Override
        public Maybe<T> otherwise(Maybe<T> maybeDefaultValue) {
            return this;
        }

        @Override
        public <U> Maybe<U> apply(Function<? super T, ? extends U> mapping) {
            return definitely(mapping.apply(theValue));
        }

        @Override
        public void apply(Procedure<? super T> proc) {
            proc.apply(theValue);
        }

        @Override
        public <U> U to(U defaultValue, Function<? super T, ? extends U> mapping) {
            return mapping.apply(theValue);
        }

        public T extract() {
            return theValue;
        }

        @Override
        public String toString() {
            return "definitely " + theValue.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DefiniteValue<?> that = (DefiniteValue<?>) o;

            return theValue.equals(that.theValue);

        }

        @Override
        public int hashCode() {
            return theValue.hashCode();
        }
    }
}
