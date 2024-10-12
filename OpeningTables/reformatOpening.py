def reformatFile(filename: str) -> None:
    full_path = "src\\main\\OpeningTables\\" + filename

    openings = {}

    with open(full_path, "r") as file:
        current_name_line = 0
        current_name = ""
        current_moves_line = 10
        for i, line in enumerate(file):
            if i == current_name_line:
                openings[line.rstrip('\n')] = ""
                current_name = line.rstrip('\n')

            if i == current_moves_line:
                linesplit = line.rstrip('\n').split()

                if len(linesplit) < 4:
                    openings.pop(current_name, None)
                else:
                    newline = ""
                    for term in linesplit:
                        if "." in term:
                            newline += term[2::] + " "
                        else:
                            newline += term + " ,"
                    openings[current_name] = newline

                current_name_line = i + 1
                current_moves_line = current_name_line + 10

    with open(full_path + "FORMATTED" + filename, "w+") as file:
        for key in openings:
            file.write(key + " \n")
            file.write(openings[key] + " \n")


reformatFile("WHITE_OPENINGS_RAW.txt")

