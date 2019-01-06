X = json.read('to_plot_gaussian.json');

fields = fieldnames(X);

for i = 1 : length(fields)
    if strcmp(fields(i), 'distribution') == 0
        sub_field = X.(fields{i});
        to_plot(sub_field, fields(i))
    end

end

X = json.read('to_plot_uniform.json');

fields = fieldnames(X);

for i = 1 : length(fields)
    if strcmp(fields(i), 'distribution') == 0
        sub_field = X.(fields{i});
        to_plot(sub_field, fields(i))
    end

end