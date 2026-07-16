package io.github.avinashio.lazyspringboot.ui.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FormTest {

    @Test
    void shouldNavigateFields() {

        Form form =
                new Form(
                        List.of(
                                new FormField(
                                        "Name",
                                        ""),
                                new FormField(
                                        "Group",
                                        "")));

        assertThat(
                form.selectedIndex())
                .isZero();

        form.nextField();

        assertThat(
                form.selectedIndex())
                .isEqualTo(1);

        form.previousField();

        assertThat(
                form.selectedIndex())
                .isZero();
    }

    @Test
    void shouldToggleEditing() {

        Form form =
                new Form(
                        List.of(
                                new FormField(
                                        "Name",
                                        "")));

        form.startEditing();

        assertThat(
                form.editing())
                .isTrue();

        form.stopEditing();

        assertThat(
                form.editing())
                .isFalse();
    }
}