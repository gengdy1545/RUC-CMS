<template>
  <div>
    <!-- Top Section -->
    <div class="top-section">
      <ul>
        <li v-for="column in topColumns" :key="column.id" @mouseover="showSubColumns(column.id)"
            @mouseleave="hideSubColumns(column.id)">
          <a :href="column.link">{{ column.name }}</a>
<!--          <ul v-if="column.showSubColumns">-->
            <li v-for="subColumn in column.children" :key="subColumn.id">
              <a :href="subColumn.link">{{ subColumn.name }}</a>
            </li>
<!--          </ul>-->
        </li>
      </ul>
    </div>

    <!-- Middle Section -->
    <div class="middle-section">
      <el-tabs v-model="activeTab">
        <el-tab-pane v-for="column in middleColumns" :key="column.id" :label="column.name">
          <a :href="column.link">{{ column.name }}</a>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- Right Section -->
    <div class="right-section">
      <ul>
        <li v-for="column in rightColumns" :key="column.id">
          <a :href="column.link">{{ column.name }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      columns: [],
      activeTab: ''
    };
  },
  computed: {
    topColumns() {
      return this.columns.filter(column => column.showPosition === 0);
    },
    middleColumns() {
      return this.columns.filter(column => column.showPosition === 1);
    },
    rightColumns() {
      return this.columns.filter(column => column.showPosition === 2);
    }
  },
  created() {
    this.fetchColumns();
  },
  methods: {
    fetchColumns() {
      axios.get('/getFrontRouters')
        .then(response => {
          this.columns = response.data;
        })
        .catch(error => {
          console.error('Error fetching columns:', error);
        });
    },
    showSubColumns(columnId) {
      const column = this.columns.find(col => col.id === columnId);
      debugger;
      if (column) {
        column.showSubColumns = true;
      }
    },
    hideSubColumns(columnId) {
      debugger;
      const column = this.columns.find(col => col.id === columnId);
      if (column) {
        column.showSubColumns = false;
      }
    }
  }
};
</script>

<style>
.top-section ul {
  list-style-type: none;
}

.top-section ul ul {
  display: none;
  position: absolute;
}

.top-section li:hover > ul {
  display: block;
}

.middle-section {
  margin-top: 20px;
}

.right-section ul {
  list-style-type: none;
  margin-top: 20px;
}
</style>
