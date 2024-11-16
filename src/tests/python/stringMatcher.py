def file_to_dict(file_path):
    file_dict = {}
    with open(file_path, 'r') as file:
        for line in file:
            key, value = line.strip().split(':')
            file_dict[key.strip()] = value.strip()
    return file_dict

def compare_files(expected_file, actual_file):
    expected_dict = file_to_dict(expected_file)
    actual_dict = file_to_dict(actual_file)
    
    for key in expected_dict:
        if key in expected_dict and key in actual_dict:
            if expected_dict[key] != actual_dict[key]:
                print("Expected: ",key, expected_dict[key], " but got: ", actual_dict[key])
        else:
            print("Missing : ", key, expected_dict[key])
    # for key in expected_dict:
    #     if key not in actual_dict:
    #         print(f"Missing key-value pair in actual output: {key}: {expected_dict[key]}")


# Example usage
expected_file = 'src\\tests\\python\\expectedOutput.txt'
actual_file = 'src\\tests\\python\\actualOutput.txt'

compare_files(expected_file, actual_file)
