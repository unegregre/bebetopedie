import requests
import urllib.request
import re
import os
from bs4 import BeautifulSoup


def To24(hour):
    if (hour[-2:] == "AM"):
        h = int(hour[:-3])
    else:
        h = int(hour[:-3]) + 12
    return h

if not os.path.exists("./bug_img"):
    print("creating img folder..")
    os.makedirs("./bug_img")

req = requests.get('https://animalcrossing.fandom.com/wiki/Bugs_(New_Horizons)')
soup = BeautifulSoup(req.text, "html.parser")

bugs = soup.find("table", class_="sortable").find_all("tr")

id = 0

file = open("bug.csv", "w")

for f in bugs:
    line = ""
    cols = f.find_all("td")

    name = price = loc = hours1 = hours2 = m_st_1 = m_end_1 = m_st_2 = m_end_2 = ""
    flag_hours = 0
    flag_months = 0

    if len(cols) > 0:

        id += 1

        # name
        name = "name: " + cols[0].find_all("a")[0].text

        print(name)

        # price
        price = "price: " + cols[2].text.strip()

        # location
        loc = "loc: " + cols[3].text.strip()

        # time
        hours = cols[4].small.text
        if hours == "All day":
            hours1 = "time_st: 0, time_end: 0"
        else:
            if '&' in hours:
                flag_hours = 1
                timezone = hours.split(" & ")
                h1 = timezone[0].split(" - ")
                hours1 = "time_st: " + str(To24(h1[0])) + ", time_end: " + str(To24(h1[1]))
                h2 = timezone[1].split(" - ")
                hours2 = "time_st: " + str(To24(h2[0])) + ", time_end: " + str(To24(h2[1]))

            else:
                h = hours.split(" - ")
                hours1 = "time_st: " + str(To24(h[0])) + ", time_end: " + str(To24(h[1]))

        # months
        months = ""
        for m in cols[5:17]:
            months += m.text.strip()
        # print(months)
        start = [m.start() for m in re.finditer('-✓', months)]
        end = [m.start() for m in re.finditer('✓-', months)]

        # All year
        if len(start) == 0 and len(end) == 0:
            m_st_1 = 0
            m_end_1 = 0
        # from january
        elif len(start) == 0 and len(end) > 0:
            m_st_1 = 1
            m_end_1 = end[0] + 1
        # until december
        elif len(start) > 0 and len(end) == 0:
            m_st_1 = start[0] + 2
            m_end_1 = 12
        # two periods
        elif len(start) > 1 or len(end) > 1:
            flag_months = 1

            if start[1] > end[1]:
                m_st_1 = start[1] + 2
                m_end_1 = end[0] + 1
                m_st_2 = start[0] + 2
                m_end_2 = end[1] + 1
            else:
                m_st_1 = start[0] + 2
                m_end_1 = end[0] + 1
                m_st_2 = start[1] + 2
                m_end_2 = end[1] + 1
        # standard case
        else:
            m_st_1 = start[0] + 2
            m_end_1 = end[0] + 1

        # image & code generation
        code = "id: " + str(id) + "," + name + "," + price + "," + loc + ","
        code_bis = ""
        urllib.request.urlretrieve(cols[1].find_all("a", href=True)[0]['href'], "./bug_img/bug" + str(id) + ".jpg")

        # duplicate if needed
        code += hours1
        code_bis += hours1
        code += ", month_st: " + str(m_st_1) + ", month_end: " + str(m_end_1)
        code_bis += ", month_st: " + str(m_st_1) + ", month_end: " + str(m_end_1)
        file.write(code + '\n')
"""
        if flag_hours == 1 or flag_months == 1:
            id += 1
            code_bis = "id: " + str(id) + "," + name + "," + price + "," + loc + ","
            urllib.request.urlretrieve(cols[1].find_all("a", href=True)[0]['href'], "./bug_img/bug" + str(id) + ".jpg")

        if flag_hours == 1:
            code += hours1
            code_bis += hours2
        else:
	    code += hours1
	    code_bis += hours1
        if flag_months == 1:
            code += ", month_st: " + str(m_st_1) + ", month_end: " + str(m_end_1)
            code_bis += ", month_st: " + str(m_st_2) + ", month_end: " + str(m_end_2)
        else:
    		code += ", month_st: " + str(m_st_1) + ", month_end: " + str(m_end_1)
    		code_bis += ", month_st: " + str(m_st_1) + ", month_end: " + str(m_end_1)

        file.write(code + '\n')
        if flag_hours == 1 or flag_months == 1:
            file.write(code_bis + '\n')
"""
