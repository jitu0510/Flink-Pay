import React from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, Cell,Pie,PieChart,Label } from 'recharts';
import '../CSS/DashboardChartComponent.css'; // Import your custom CSS for styling
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import { getEmployeesExperienceData} from './../Services/UserService'
import { useState,useEffect } from 'react';

const ChartComponent = () => {
    const [Piedata, setPiedata] = useState([]);
    const [employeeData,setEmployeeData] = useState([]);
    const history = useHistory();
  
  const colors = ['red', 'blue', 'green', 'orange', 'purple'];

useEffect(() => {
    // Fetch your data from the API
     localStorage.setItem('page','/dashboard/overview');
   
    axios.get('http://49.249.29.5:8091/project-status-data')
      .then(response => {
        // Assuming your API response structure matches the provided example
        setPiedata([
          { name: 'Created', value: response.data.created },
          { name: 'Ongoing', value: response.data.onGoing },
          { name: 'Not Applicable', value: response.data.na },
          { name: 'Completed', value: response.data.completed },
        ]);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []);

  useEffect(() => {
    // Fetch your data from the API
    getEmployeesExperienceData()
      .then(response => {
        // Assuming your API response structure matches the provided example
        setEmployeeData([
          { name: '0-5', 'Experience in years': response.data.zeroToFive },
          { name: '6-10', 'Experience in years': response.data.sixToTen },
          { name: '11-15', 'Experience in years': response.data.elevenToFifteen },
          { name: '16-20', 'Experience in years': response.data.sixteenToTwenty },
          { name: '>20', 'Experience in years': response.data.greaterThanTwenty }
        ]);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []);

  


  // Array of colors
  const piecolors = ['#FF5733', '#33FF57', '#5733FF', '#FFD700'];

  return (
    <div>
    <div className="chart-card">
      <h2>Employees</h2>
      <BarChart width={300} height={200} data={employeeData}>
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="Experience in years" fill="none">
          {employeeData.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
          ))}
        </Bar>
        <label value="Experience"/>
      </BarChart>
    </div>
    <div className="chart-card">
    <h2>Projects</h2>
    <PieChart width={250} height={200}>
      <Pie 
        data={Piedata}
        cx={125}
        cy={70}
        innerRadius={30}
        outerRadius={70}
        fill="#8884d8"
        paddingAngle={0}
        dataKey="value"
      >
        {Piedata.map((entry, index) => (
          <Cell key={`cell-${index}`} fill={piecolors[index % piecolors.length]} />
        ))}
        <Label value="Status" position="center" />
      </Pie>
      <Tooltip />
      <Legend />
    </PieChart>
    </div>
   
    </div>
  );
};

export default ChartComponent;
