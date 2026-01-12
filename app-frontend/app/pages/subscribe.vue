<template>
    <div class="container">
        <h1>Choose Your Plan</h1>
        <div class="packages">
            <div v-for="pkg in subscriptionStore.packages" :key="pkg.type" class="package">
                <h2>{{ pkg.type }}</h2>
                <p>
                    <strong>Price:</strong>
                    <span v-if="typeof pkg.price === 'number'">${{ pkg.price }}/month</span>
                    <span v-else>{{ pkg.price }}</span>
                </p>
                <ul>
                    <li>{{ pkg.max_users }} Users</li>
                    <li>{{ pkg.max_admins }} Admins</li>
                    <li>{{ pkg.max_support }} Support</li>
                </ul>
                <div v-if="pkg.type !== 'FREE'" class="form-group">
                    <label :for="'org-name-' + pkg.type" class="form-label">Organization Name</label>
                    <input
                        type="text"
                        :id="'org-name-' + pkg.type"
                        v-model="organizationName"
                        class="form-input"
                    />
                </div>
                <button @click="selectPlan(pkg.type)">Select Plan</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useSubscriptionStore } from '~/stores/subscription';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const subscriptionStore = useSubscriptionStore();
const router = useRouter();
const organizationName = ref('');

const selectPlan = async (planType) => {
    let orgName = null;
    if (planType !== 'FREE') {
        orgName = organizationName.value;
        if (!orgName) {
            // You might want to show an error to the user here
            alert('Organization name is required for this plan.');
            return;
        }
    }
    const { success } = await subscriptionStore.selectPlan(planType, orgName);
    if (success) {
        // Redirect to a different page after successful subscription, e.g., dashboard
        router.push('/'); 
    }
};
</script>

<style scoped>
.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
}

h1 {
    text-align: center;
    margin-bottom: 2rem;
}

.packages {
    display: flex;
    justify-content: center;
    gap: 2rem;
}

.package {
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 2rem;
    text-align: center;
    width: 300px;
}

.package h2 {
    margin-bottom: 1rem;
}

.package p {
    margin-bottom: 1rem;
}

.package ul {
    list-style: none;
    padding: 0;
    margin-bottom: 2rem;
}

.package ul li {
    margin-bottom: 0.5rem;
}
</style>
