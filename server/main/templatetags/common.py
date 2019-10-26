from django import template
from django.template.defaultfilters import stringfilter

import markdown as md

register = template.Library()


class SetVarNode(template.Node):
    def __init__(self, var_name, var_value):
        self.var_name = var_name
        self.var_value = var_value

    def render(self, context):
        try:
            value = template.Variable(self.var_value).resolve(context)
        except template.VariableDoesNotExist:
            value = ""
        context[self.var_name] = value

        return u""


@register.tag(name='set')
def _set(parser, token):
    parts = token.split_contents()
    if len(parts) < 4:
        raise template.TemplateSyntaxError("'set' tag must be of the form: {% set <var_name> = <var_value> %}")

    return SetVarNode(parts[1], parts[3])


@register.filter
@stringfilter
def escape_tag(value):
    return value.replace('<', '&lt;').replace('>', '&gt;')


@register.filter
@stringfilter
def mark2html(value):
    return md.markdown(value, extensions=['gfm'])
