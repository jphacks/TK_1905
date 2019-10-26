from django.core.exceptions import FieldError


class ModelFormWithFormSetMixin:
    def __init__(self, form_kwargs={}, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.formset = self.formset_class(
            form_kwargs=form_kwargs,
            instance=self.instance,
            data=self.data if self.is_bound else None,
            files=self.files if self.is_bound else None,
        )
        try:
            queryset = self.formset.queryset.order_by('created_at')
            queryset.first()
            self.formset = self.formset_class(
                form_kwargs=form_kwargs,
                instance=self.instance,
                data=self.data if self.is_bound else None,
                files=self.files if self.is_bound else None,
                queryset=queryset
            )
        except FieldError:
            pass

    def is_valid(self):
        return super().is_valid() and self.formset.is_valid()

    def save(self, commit=True):
        saved_instance = super().save(commit)
        self.formset.save(commit)
        return saved_instance


class BootstrapFormMixin:
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        for f_name in self.fields.keys():
            self.fields[f_name].widget.attrs['class'] = 'form-control'
