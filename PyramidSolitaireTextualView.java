package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.io.IOException;
import java.util.Objects;

public class PyramidSolitaireTextualView implements PyramidSolitaireView {
    private final PyramidSolitaireModel<?> model;
    private Appendable out;
    // ... any other fields you need

    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model) {
        this.model = model;
    }

    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model, Appendable out) {
        this.model = model;
        this.out = out;
    }

    @Override
    public String toString() {
        return model.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        else if(this.hashCode() == o.hashCode())
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    /**
     * Renders a model in some manner (e.g. as text, or as graphics, etc.).
     *
     * @throws IOException if the rendering fails for some reason
     */
    @Override
    public void render() throws IOException {
        this.out.append(model.toString());
    }
}
